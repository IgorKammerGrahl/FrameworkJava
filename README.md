# contract-framework — Design by Contract

Biblioteca Java que implementa **Projeto por Contrato** (*Design by
Contract*): validação automática de **pré-condições**, **pós-condições** e
**invariantes** de objetos, através de anotações e um proxy dinâmico.

Empacotada como um artefato Gradle/Maven real (`br.com.igorkg:contract-framework`),
publicável no repositório local e consumível por qualquer outro projeto Java
como dependência — veja [`exemplo-uso/`](exemplo-uso).

Trabalho final da disciplina de Programação Orientada a Objetos II (POOII).

## A ideia

Em vez de o desenvolvedor escrever `if (amount == null) throw ...` dentro de
cada método, ele declara o contrato direto na assinatura usando anotações:

```java
public interface WalletService {
    void deposit(@NotNull @MinValue(1) Integer amount);
}
```

O framework intercepta toda chamada ao método através de um
[`Proxy`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Proxy.html)
dinâmico, valida os parâmetros **antes** de executar o método real e o
retorno/invariantes **depois**. Se alguma regra for quebrada, uma exceção
específica da fase é lançada e a implementação nunca chega a rodar com dados
inválidos.

## Estrutura do repositório

```
FrameworkJava/                          ← a biblioteca (projeto Gradle)
├── build.gradle  settings.gradle  gradlew
├── README.md   arquitetura.puml
└── src/main/java/br/com/igorkg/contract/
    ├── core/
    │   ├── ContractEngine.java             # builder fluente / ponto de entrada
    │   ├── ValidatorRegistry.java          # registro de validadores por anotação
    │   └── ContractInvocationHandler.java  # proxy que aplica o contrato em cada chamada
    ├── validators/
    │   ├── ContractValidator.java          # interface (Strategy) de um validador
    │   ├── NotNull.java / NotNullValidator.java
    │   └── MinValue.java / MinValueValidator.java
    └── exceptions/
        ├── ContractViolationException.java # classe base sealed
        ├── PreConditionException.java
        ├── PostConditionException.java
        └── InvariantViolationException.java

exemplo-uso/                            ← OUTRO projeto Gradle, consome a lib
├── build.gradle  settings.gradle  gradlew
└── src/main/java/br/com/igorkg/exemplo/
    ├── WalletService.java       # contrato de exemplo, não faz parte da lib
    ├── WalletServiceImpl.java   # implementação de exemplo
    └── App.java                 # demonstração executável
```

A biblioteca em si (`br.com.igorkg.contract.*`) não contém nenhum domínio de
negócio — `WalletService` é só um exemplo de consumo, por isso vive no
projeto `exemplo-uso/`.

## Diagrama de classes

O arquivo [`arquitetura.puml`](arquitetura.puml) (PlantUML) documenta as
classes e os 5 tipos de relacionamento entre elas: composição, agregação,
associação, dependência e generalização/realização.

## Conceitos de OOP aplicados

- **Interfaces e contratos** — `WalletService`, `ContractValidator<A, T>`
- **Herança selada (`sealed`)** — hierarquia fechada de exceções de contrato
- **Generics** — `ContractValidator<A extends Annotation, T>`
- **Anotações customizadas em runtime** — `@NotNull`, `@MinValue`
- **Reflexão** — leitura de anotações, campos e métodos via `java.lang.reflect`
- **Proxy dinâmico** — `Proxy.newProxyInstance` intercepta as chamadas
- **Strategy** — cada `ContractValidator` é uma estratégia de validação plugável
- **Builder fluente** — `ContractEngine.setup().addValidator(...).protect(...)`

## Como gerar e publicar o JAR

Requer JDK 21+. O Gradle Wrapper já está incluso, não precisa instalar nada.

```bash
./gradlew build              # compila e gera build/libs/contract-framework-1.0.0.jar
./gradlew publishToMavenLocal   # instala a lib em ~/.m2 para outros projetos usarem
```

O `build.gradle` também gera `-sources.jar` e `-javadoc.jar` automaticamente.

## Como usar a lib em outro projeto

Depois de publicada com `publishToMavenLocal`, qualquer projeto Gradle
consegue declará-la como dependência normal:

```groovy
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation 'br.com.igorkg:contract-framework:1.0.0'
}
```

O projeto [`exemplo-uso/`](exemplo-uso) faz exatamente isso — é um segundo
projeto Gradle independente, sem nenhuma classe do framework copiada, que só
declara a dependência acima e importa `br.com.igorkg.contract.*`.

```bash
cd exemplo-uso
./gradlew run
```

## Saída esperada

```
=== Design-by-Contract demo (via contract-framework:1.0.0) ===

1) Valid call: deposit(100)
  [WalletServiceImpl] deposited 100 -> balance = 100

2) Invalid call: deposit(null)
  Caught PreConditionException -> [Contract Violation] Pre-condition: value must not be null

3) Invalid call: deposit(0)
  Caught PreConditionException -> [Contract Violation] Pre-condition: value must be >= 1 but was 0
```

A primeira chamada é válida e passa pelo proxy sem erro. As duas seguintes
quebram o contrato declarado em `WalletService.deposit` — o proxy rejeita a
entrada **antes** de `WalletServiceImpl` ser executado (fail-fast).
