<%--
  Created by IntelliJ IDEA.
  User: arman
  Date: 6/21/2023
  Time: 3:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- c:out ; c:forEach etc. -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Formatting (dates) -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!-- form:form -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!-- for rendering errors on PUT routes -->
<%@ page isErrorPage="true" %>

<html data-bs-theme="dark">
<head>
  <link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css">
  <script type="text/javascript" src="/js/app.js"></script>
  
  <%-- TODO: PAGE TITLE --%>
  <title>Title</title>
</head>
<body>
<nav class="navbar navbar-expand-lg bg-body-tertiary">
  <div class="container-fluid">
    <a class="navbar-brand fs-3" href="/dashboard">
      Overseer
    </a>
    <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent"
        aria-expanded="false"
        aria-label="Toggle navigation"
    >
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <%-- NAV LIST ITEMS GO HERE --%>
        <li class="nav-item">
          <a class="nav-link" aria-current="page" href="/dashboard">
            Dashboard
          </a>
        </li>
      </ul>
      
      <!-- TODO: About href -->
      <a class="nav-link mx-3" aria-current="page" href="#">About</a>
      <a class="btn btn-danger" href="/logout" role="button">Log Out</a>
    </div>
  </div>
</nav>
<main class="container p-5">
  <div class="d-flex justify-content-center">
    <div class="card w-100 mb-5">
      <div class="card-header text-center fs-3">
        ${project.projectName}
      </div>
      <div class="card-body">
        <h3 class="mb-3">Lead by: ${project.lead.username}</h3>
        <p class="mb-3">Current tasks:</p>
        <ul class="list-group">
          <c:forEach var="task" items="${project.tasks}">
            <li class="list-group-item">
              <p class="fw-bold">
                Added by <span>${task.addedBy}</span>
                on <fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${task.createdAt}"/>
              </p>
                ${task.description}
            </li>
          </c:forEach>
        </ul>
      </div>
    </div>
  </div>
  <c:if test="${project.team.contains(user)}">
    <hr class="mb-5">
    <div class="mb-3">
      <form:form action="/projects/${project.id}/tasks/create" method="post" modelAttribute="task">
        <form:input type="hidden" path="addedBy" value="${user.username}"/>
        <form:input type="hidden" path="project" value="${project.id}"/>
        <form:label class="form-label" path="description">
          Add a task ticket for the team:
        </form:label>
        <form:textarea class="form-control" path="description"/>
        <p class="text-danger">
          <form:errors path="description"/>
        </p>
        <div class="d-flex justify-content-end">
          <button type="submit" class="btn btn-outline-success">
            Submit
          </button>
        </div>
      </form:form>
    </div>
  </c:if>
</main>
</body>
</html>