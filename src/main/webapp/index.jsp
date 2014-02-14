<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <!DOCTYPE html>
    <html lang="en">
        <head>
            <meta charset="utf-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <meta name="description" content="">
            <meta name="author" content="">
            <link rel="shortcut icon" href="../../assets/ico/favicon.ico">

            <title>Sitemindr (GAE)</title>

            <!-- Bootstrap core CSS -->
            <link href="css/bootstrap.min.css" rel="stylesheet">

            <!-- Custom styles for this template -->


            <!-- Just for debugging purposes. Don't actually copy this line! -->
            <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->

            <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
            <!--[if lt IE 9]>
              <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
              <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
            <![endif]-->
        </head>

        <body>

            <div class="container">

                <!-- Static navbar -->
                <div class="navbar navbar-default" role="navigation">
                    <div class="container-fluid">
                        <div class="navbar-header">

                            <a class="navbar-brand" href="#">Sitemindr (GAE)</a>
                        </div>
                        <div class="navbar">
                            <ul class="nav navbar-nav">
                                <li class="active">

                                    <%
                                        UserService userService = UserServiceFactory.getUserService();
                                        User user = userService.getCurrentUser();
                                        if (user != null) {
                                            pageContext.setAttribute("user", user);
                                    %>
                                    <p>${fn:escapeXml(user.nickname)}
                                        <a href="<%= userService.createLogoutURL(request.getRequestURI())%>">sign out</a></p>
                                        <%
                                        } else {
                                        %>
                                    <p>
                                        <a href="<%= userService.createLoginURL(request.getRequestURI())%>">Sign in</a>
                                    </p>
                                    <%
                                        }
                                    %>

                                </li>
                                <li class="dropdown">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">Configuration<b class="caret"></b></a>
                                    <ul class="dropdown-menu">
                                        <li><a href="#">Sites</a></li>
                                        <li><a href="#">Interested Parties</a></li>
                                    </ul>
                                </li>
                            </ul>
                        </div><!--/.nav-collapse -->
                    </div><!--/.container-fluid -->
                </div>

                <!-- Main component for a primary marketing message or call to action -->
                <div class="jumbotron">
                    <h2>Sites</h2>
                    <p>
                        <%
                            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                            Query query = new Query("Site");
                            List<Entity> sites = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
                            if (sites.isEmpty()) {
                        %>
                    <p>There are no sites configured</p>
                    <%
                    } else {
                        if (user != null) {
                            for (Entity site : sites) {
                                pageContext.setAttribute("name", site.getKey().getName());
                                pageContext.setAttribute("available", ("true".equals(site.getProperty("available").toString()) ? "" : "not ") + "available");

                    %>
                    <p>${fn:escapeXml(name)} - ${fn:escapeXml(available)}</p>
                    <%             }
                            }
                        }
                    %>

                    </p>
                </div>

            </div> <!-- /container -->


            <!-- Bootstrap core JavaScript
            ================================================== -->
            <!-- Placed at the end of the document so the pages load faster -->
            <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
            <script src="js/bootstrap.min.js"></script>
        </body>
    </html>


