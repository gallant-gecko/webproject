import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UpdateTaskStatus")
public class UpdateTaskStatus extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int taskId = Integer.parseInt(request.getParameter("task_id"));
        String updateSql = "UPDATE tasks SET status = 'Complete' WHERE task_id = ?";

        // Ensure the database connection is established
        DBConnection.getDBConnection();
        try (Connection connection = DBConnection.connection;
             PreparedStatement stmt = connection.prepareStatement(updateSql)) {

            stmt.setInt(1, taskId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Redirect to the SearchTasks servlet to display the list of tasks
        response.sendRedirect("SearchTasks");
    }
}
