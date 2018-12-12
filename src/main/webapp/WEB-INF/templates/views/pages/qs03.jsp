<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../partials/head.jsp"/>

<h4>List Updated Envelopes</h4>
<p>This example lists the user's envelopes that have changed status in the last 10 days.</p>

<form class="eg" action="" method="post" data-busy="form">
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <button type="submit" class="btn btn-primary">Submit</button>
</form>

<jsp:include page="../partials/foot.jsp"/>
