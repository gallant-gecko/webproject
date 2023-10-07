import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SearchTasks")
public class SearchTasks extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTitle = request.getParameter("searchTitle");
        String searchDueDate = request.getParameter("searchDueDate");
        String searchPriority = request.getParameter("searchPriority");

        // Construct the SQL query based on the provided criteria
        StringBuilder sql = new StringBuilder("SELECT * FROM tasks WHERE 1=1");
        if (searchTitle != null && !searchTitle.isEmpty()) {
            sql.append(" AND title LIKE ?");
        }
        if (searchDueDate != null && !searchDueDate.isEmpty()) {
            sql.append(" AND due_date = ?");
        }
        if (searchPriority != null && !searchPriority.isEmpty()) {
            sql.append(" AND priority = ?");
        }

        // Start constructing the HTML response
        StringBuilder htmlResponse = new StringBuilder();
        htmlResponse.append("<html><head><title>Search Results</title><link rel='stylesheet' type='text/css' href='styles.css'></head><body>");
        htmlResponse.append("<h1>Search Results</h1>");

        // Ensure the database connection is established
        DBConnection.getDBConnection();
        try (Connection connection = DBConnection.connection;
             PreparedStatement stmt = connection.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (searchTitle != null && !searchTitle.isEmpty()) {
                stmt.setString(paramIndex++, "%" + searchTitle + "%");
            }
            if (searchDueDate != null && !searchDueDate.isEmpty()) {
                stmt.setString(paramIndex++, searchDueDate);
            }
            if (searchPriority != null && !searchPriority.isEmpty()) {
                stmt.setString(paramIndex++, searchPriority);
            }

            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
                htmlResponse.append("<p>No tasks found matching the criteria.</p>");
            } else {
                htmlResponse.append("<table class='task-table'>");
                htmlResponse.append("<tr><th>Title</th><th>Details</th><th>Due Date</th><th>Priority</th><th>Status</th><th>Action</th></tr>");
                while (rs.next()) {
                    htmlResponse.append("<tr>");
                    htmlResponse.append("<td>").append(rs.getString("title")).append("</td>");
                    htmlResponse.append("<td>").append(rs.getString("details")).append("</td>");
                    htmlResponse.append("<td>").append(rs.getDate("due_date")).append("</td>");
                    htmlResponse.append("<td>").append(rs.getString("priority")).append("</td>");
                    if ("Not Complete".equals(rs.getString("status"))) {
                        htmlResponse.append("<td class='status-not-complete'><form action='UpdateTaskStatus' method='post'>");
                        htmlResponse.append("<input type='hidden' name='task_id' value='").append(rs.getInt("task_id")).append("'>");
                        htmlResponse.append("<input type='submit' class='btn-complete' value='Mark as Complete'></form></td>");
                    } else {
                        htmlResponse.append("<td class='status-complete'>Complete</td>");
                    }
                    htmlResponse.append("<td><form action='DeleteTask' method='post'>");
                    htmlResponse.append("<input type='hidden' name='task_id' value='").append(rs.getInt("task_id")).append("'>");
                    htmlResponse.append("<input type='submit' class='btn-delete' value='Delete'></form></td>");
                    htmlResponse.append("</tr>");
                }
                htmlResponse.append("</table>");
            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            htmlResponse.append("<p>Error occurred while fetching the tasks. Please try again later.</p>");
        }

        htmlResponse.append("<br><a href='/webproject/searchTasks.html'>Back to Search</a>");
        htmlResponse.append("</body></html>");

        // Send the constructed HTML response to the client
        response.getWriter().print(htmlResponse.toString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
