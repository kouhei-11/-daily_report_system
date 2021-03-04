package controllers.waruine;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Iine;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class WaruineServlet
 */
@WebServlet("/waruine")
public class WaruineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public WaruineServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    public void doGet (HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
            {
            this.doPost(request, response);
            }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        ServletContext sc = this.getServletContext();
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        Iine i = null;

        try{
            i = em.createNamedQuery("getAllIines", Iine.class)
                    .setParameter("employee",login_employee )
                    .setParameter("report", r)
                    .getSingleResult();

            em.getTransaction().begin();
            em.remove(i);
            em.getTransaction().commit();
            em.close();

            sc.removeAttribute("i");
        } catch(NoResultException ex) {
            sc.setAttribute("iine_count", 0);
            em.close();
        }












        response.sendRedirect(request.getContextPath() + "/reports/show?id="+r.getId());

    }
}


