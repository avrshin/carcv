package org.carcv.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carcv.beans.EntryBean;
import org.carcv.model.Entry;

/**
 * Servlet implementation class CarTableServlet
 */
@WebServlet("/servlet/CarTableServlet")
public class CarTableServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private EntryBean bean;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CarTableServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		ArrayList<Entry> entries = (ArrayList<Entry>) bean.getAll();

		//write page
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\">");
		out.println("<title>Car database</title>");
		out.println("<style type=\"text/css\">");
		out.println("#table {");
		out.println("	text-align: center;");
		out.println("}");
		out.println("</style>");
		out.println("</head>");

		out.println("<body>");
		out.println("<table style=\"border: 1px solid #C0C0C0;\">");
		out.println("<tr>");
		out.println("<th style=\"width: 160px; height: 15px; background-color: #B0C4DE;\">Car preview</th>");
		out.println("<th style=\"width: 10%; height: 15px; background-color: #B0C4DE;\">Date</th>");
		out.println("<th style=\"width: 15%; height: 15px; background-color: #B0C4DE;\">Licence plate</th>");
		out.println("<th style=\"width: 20%; height: 15px; background-color: #B0C4DE;\">Location</th>");
		out.println("<th style=\"width: 20%; height: 15px; background-color: #B0C4DE;\">Video</th>");
		out.println("<th style=\"width: 20%; height: 15px; background-color: #B0C4DE;\">Pictures</th>");
		out.println("<th style=\"width: 15%; height: 15px; background-color: #B0C4DE;\">Report</th>");
		out.println("</tr>");

		DateFormat dateFormat = new SimpleDateFormat("dd. MM. yyyy");
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");

		String date, time;
		String licencePlate;
		String location;
		String videoURL;
		String previewURL;

		for (Entry e : entries) {
			// initialize
			date = dateFormat.format(e.getData().getTimestamp());
			time = timeFormat.format(e.getData().getTimestamp());
			licencePlate = e.getData().getLicencePlate().getOrigin() + " - "
					+ e.getData().getLicencePlate().getOrigin();
			location = e.getData().getLocation().toString();
			videoURL = e.getData().getVideo().getURL();
			previewURL = e.getPreview().getURL();

			// write
			out.println("<tr>");
			out.println("<td style=\"\"><img");
			out.println("src=\"" + previewURL + "\"");
			out.println("style=\"border: 2px\" width=\"150\" alt=\"Car\"></td>");
			out.println("<td>" + date + "\n" + time + "</td>");
			out.println("<td>" + licencePlate + "</td>");
			out.println("<td>" + location + "</td>");
			out.println("<td><a href=\"" + videoURL
					+ "\" target=\"_top\">View video</a></td>");
			out.println("<td><a href=\"" + previewURL
					+ "\" target=\"_top\">View pictures</a></td>");
			out.println("<td><a href=\"" + "/servlet/GenerateReport"
					+ "\" target=\"_top\">Generate report</a></td>");
			out.println("</tr>");
		}

		out.println("</table>");
		out.println("</body>");
		out.println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
