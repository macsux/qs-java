# DocuSign Quick Start examples for Java

Repository: [qs-java](https://github.com/docusign/qs-java)

This quickstart provides straight-forward
code examples for quickly
trying the DocuSign eSignature API with the 
[Java SDK](https://github.com/docusign/docusign-java-client).

The repo includes:

1. Embedding a signing ceremony in your web application.
[Source.](https://github.com/docusign/qs-java/blob/master/src/main/java/com/docusign/controller/QS01EmbedSigningCeremonyController.java) 
2.  Sending a signing request via an email to the signer.
[Source.](https://github.com/docusign/qs-java/blob/master/src/main/java/com/docusign/controller/QS02SendEnvelopeController.java)
3. Listing updated envelopes in the user's account, including their status.
[Source.](https://github.com/docusign/qs-java/blob/master/src/main/java/com/docusign/controller/QS03ListEnvelopesController.java)

These examples do not include authentication. Instead,
use the DocuSign DevCenter's
[OAuth token generator](https://developers.docusign.com/oauth-token-generator)
to create an access token.

**For a complete set of Java examples,** including 
authentication via the OAuth Authentication Grant flow, see the
[Java Example Launcher](https://github.com/docusign/eg-03-java-auth-code-grant)

A [Java JWT authentication example](https://github.com/docusign/eg-01-java-jwt)
is also available.

## Installation

### Compatibility
This example uses Java 8 and the Java Spring Boot webapp framework. 
The example code and SDK can be used with Java 1.7 or later.

### Installation steps
* Download or clone this repository.
* The project includes a Maven pom file.
* Configure the project by updating the three example files (see below).
* The project's main class is `com.docusign.App`
* Note that IntelliJ Community Edition does not directly support
  Spring Boot applications.


### Configure the example files' settings
Each quick start example is a self-contained file. You need to
configure each:

* [QS01EmbedSigningCeremonyController.java](https://github.com/docusign/qs-java/blob/master/src/main/java/com/docusign/controller/QS01EmbedSigningCeremonyController.java)
* [QS02SendEnvelopeController.java](https://github.com/docusign/qs-java/blob/master/src/main/java/com/docusign/controller/QS02SendEnvelopeController.java)
* [QS03ListEnvelopesController.java](https://github.com/docusign/qs-java/blob/master/src/main/java/com/docusign/controller/QS03ListEnvelopesController.java)

You will configure them with the following data:

 * **Access token:** Use the [OAuth Token Generator](https://developers.docusign.com/oauth-token-generator).
   To use the token generator, you'll need a
   [free DocuSign Developer's account.](https://go.docusign.com/o/sandbox/)

   Each access token lasts 8 hours, you will need to repeat this process
   when the token expires. You can use the same access token for
   multiple examples.

 * **Account Id:** After logging into the [DocuSign Sandbox system](https://demo.docusign.net),
   you can copy your Account Id from the dropdown menu by your name. See the figure:

   ![Figure](https://raw.githubusercontent.com/docusign/qs-java/master/documentation/account_id.png)
 * **Signer name and email:** Remember to try the DocuSign signing ceremony using both a mobile phone and a regular
   email client.

## Run the examples

Build and run the examples. The default url for the examples is http://localhost:8080


## Support, Contributions, License

Submit support questions to [StackOverflow](https://stackoverflow.com). Use tag `docusignapi`.

Contributions via Pull Requests are appreciated.
All contributions must use the MIT License.

This repository uses the MIT license, see the
LICENSE file.
