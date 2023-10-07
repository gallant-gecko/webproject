import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DeleteTask")
public class DeleteTask extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int taskId = Integer.parseInt(request.getParameter("task_id"));

        // Construct the SQL query to delete the task
        String sql = "DELETE FROM tasks WHERE task_id = ?";

        // Ensure the database connection is established
        DBConnection.getDBConnection();
        try (Connection connection = DBConnection.connection;
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, taskId);
            stmt.executeUpdate();

            // Redirect back to the SearchTasks servlet to display the updated list
            response.sendRedirect("SearchTasks");

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the error, maybe log it or display an error message
        }
    }
}
