package org.cassandra_viewer.type_editor;

import org.apache.cassandra.thrift.NotFoundException;
import org.apache.thrift.TException;
import org.cassandra_viewer.Controller;
import org.cassandra_viewer.data.CassandraBrowser;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: captain-protect
 * Date: 4/8/12
 * Time: 11:40 AM
 */
public class TypeEditor extends Controller {
    private final CassandraBrowser browser;
    private TypeSchema schema;

    public TypeEditor(Controller controller, CassandraBrowser browser) {
        super(controller);
        this.browser = browser;
        try {
            schema = TypeSchema.createDeafult(browser);
        } catch (TException e) {


        } catch (NotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void doGet() throws IOException, ServletException {
        forward("/WEB-INF/type_editor.jsp");
    }
}
