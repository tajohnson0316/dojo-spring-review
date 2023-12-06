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

<main class="container-fluid p-4">
  <h1 class="mb-3">Welcome, ${username}!</h1>
  <div class="d-flex justify-content-between align-items-end mb-3">
    <p>Projects you can join:</p>
    <a href="/projects/new/form" class="btn btn-outline-success" role="button">
      + New Project
    </a>
  </div>
  <div class="d-flex justify-content-center">
    <div class="card w-100 mb-5">
      <div class="card-body">
        <table class="table table-striped table-bordered">
          <thead>
          <tr>
            <th scope="col">Project Name</th>
            <th scope="col">Project Lead</th>
            <th scope="col">Due Date</th>
            <th scope="col">Actions</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="project" items="${unjoinedProjects}">
            <tr>
              <td>
                <a href="/projects/${project.id}">${project.projectName}</a>
              </td>
              <td>
                  ${project.lead.username}
              </td>
              <td>
                <fmt:formatDate value="${project.dueDate}"/>
              </td>
              <td>
                <div class="d-flex justify-content-center">
                  <form action="/projects/${project.id}/join" method="post">
                    <button type="submit" class="btn btn-primary">
                      Join Team
                    </button>
                  </form>
                </div>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <hr class="mb-5">
  <p>Projects you're a part of:</p>
  <div class="d-flex justify-content-center">
    <div class="card w-100">
      <div class="card-body">
        <table class="table table-striped table-bordered">
          <thead>
          <tr>
            <th scope="col">Project Name</th>
            <th scope="col">Project Lead</th>
            <th scope="col">Due Date</th>
            <th scope="col">Actions</th>
          </tr>
          </thead>
          <tbody>
          <%-- TODO: LIST OF JOINED PROJECTS --%>
          <c:forEach var="project" items="${projectsJoined}">
            <tr>
              <td>
                <a href="/projects/${project.id}">${project.title}</a>
              </td>
              <td>
                  ${project.lead.userName}
              </td>
              <td>
                <fmt:formatDate value="${project.dueDate}"/>
              </td>
              <td>
                  <%-- CONDITIONAL RENDERING OF EDIT OPTION FOR TEAM LEADS --%>
                <c:choose>
                  <c:when test="${project.lead.id.equals(userId)}">
                    <div class="d-flex justify-content-center">
                      <form action="/projects/delete/${project.id}" method="post">
                        <div class="btn-group" role="group">
                          <a href="/projects/edit/${project.id}" class="btn btn-warning">
                            Edit
                          </a>
                          <input type="hidden" name="_method" value="delete">
                          <button type="submit" class="btn btn-danger">Delete</button>
                        </div>
                      </form>
                    </div>
                  </c:when>
                  <c:otherwise>
                    <div class="d-flex justify-content-center">
                      <form action="/projects/${project.id}/leave" method="post">
                        <button type="submit" class="btn btn-outline-danger">
                          Leave Team
                        </button>
                      </form>
                    </div>
                  </c:otherwise>
                </c:choose>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</main>
</body>
</html>