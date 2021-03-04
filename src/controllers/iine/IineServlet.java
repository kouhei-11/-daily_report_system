package controllers.iine;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;
import models.Iine;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class IineServlet
 */
@WebServlet("/iine")
public class IineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IineServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        ServletContext sc = this.getServletContext();
        HttpSession session = request.getSession();
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        Iine i=null;
         try{
             i = em.createNamedQuery("getAllIines", Iine.class)
                    .setParameter("employee",login_employee )
                    .setParameter("report", r)
                    .getSingleResult();
        } catch(NoResultException ex) {
        }

        if (i == null ) {
            i = new Iine();
            i.setEmployee((Employee) request.getSession().getAttribute("login_employee"));
            i.setReport((Report) session.getAttribute("report"));

            sc.setAttribute("iine_count", i);
            sc.setAttribute("iines", i);
        }
            em.getTransaction().begin();
            em.persist(i);
            em.getTransaction().commit();
            em.close();


        response.sendRedirect(request.getContextPath() + "/reports/show?id=" + r.getId());

    }
}
