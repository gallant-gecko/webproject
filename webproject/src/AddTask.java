import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AddTask")
public class AddTask extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String details = request.getParameter("details");
        String dueDate = request.getParameter("dueDate");
        String priority = request.getParameter("priority");

        // Ensure the database connection is established
        DBConnection.getDBConnection();
        try (Connection connection = DBConnection.connection;
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO tasks (title, details, due_date, priority, status) VALUES (?, ?, ?, ?, 'Not Complete')")) {

            stmt.setString(1, title);
            stmt.setString(2, details);
            stmt.setString(3, dueDate);
            stmt.setString(4, priority);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            // Optionally, handle the error - for example, log it or display an error message to the user.
        }

        // Redirect to the SearchTasks servlet to display all tasks
        response.sendRedirect("/webproject/SearchTasks");
    }
}
