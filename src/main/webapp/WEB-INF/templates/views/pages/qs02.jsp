<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../partials/head.jsp"/>

<h4>Signing Request By Email</h4>
<p>This example sends an envelope. The signer will receive the signing request by email.</p>

<form class="eg" action="" method="post" data-busy="form">
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-primary">Submit</button>
</form>

<jsp:include page="../partials/foot.jsp"/>
