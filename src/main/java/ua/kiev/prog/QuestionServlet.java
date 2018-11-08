package ua.kiev.prog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet(name = "QuestionServlet", value = "/answer")
public class QuestionServlet extends HttpServlet {

    private static final String TEMPLATE = "<html><head><title>Questionnaire</title></head>" +
            "<body>%s<br><a href = \"/index.html\">Back to questions</a></body></html>";

    private HashMap<String, AtomicInteger> answers1 = new HashMap<>(); // Question 1 answers and votes number
    private HashMap<String, AtomicInteger> answers2 = new HashMap<>(); // Question 2 answers and votes number
    private List<String> statistics = new ArrayList<>();               // Answers statistics list

    @Override
    public void init() throws ServletException {

        answers1.put("yes", new AtomicInteger(0));
        answers1.put("no", new AtomicInteger(0));

        answers2.put("trolleybus", new AtomicInteger(0));
        answers2.put("tram", new AtomicInteger(0));
        answers2.put("subway", new AtomicInteger(0));
        answers2.put("bus", new AtomicInteger(0));
        answers2.put("car", new AtomicInteger(0));
        answers2.put("train", new AtomicInteger(0));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //processing html form data
        final String answer1 = req.getParameter("question1");
        answers1.get(answer1).incrementAndGet();

        final String answer2 = req.getParameter("question2");
        answers2.get(answer2).incrementAndGet();

        // adding record to statistics list
        final String statisticsEntry = "<br>User <b>" + req.getParameter("name") + " " + req.getParameter("surname") + " </b>(age: " + req.getParameter("age") +
                ") answers: question 1 - " + answer1 + "; question 2 - " + answer2 + ".<br>";
        statistics.add(statisticsEntry);

        // generating results page
        StringBuilder result = new StringBuilder("<b>Results:<br><br>Question 1: Do you have your own car?</b><br>");

        for (Map.Entry<String, AtomicInteger> ans : answers1.entrySet()) {
            result = result.append("<p>" + ans.getKey() + ": " + ans.getValue() + " votes;</p>");
        }

        result = result.append("\n<b>Question 2: What is your most frequently used transport?</b><br>");
        for (Map.Entry<String, AtomicInteger> ans : answers2.entrySet()) {
            result = result.append("<p>" + ans.getKey() + ": " + ans.getValue() + " votes;</p>");
        }
        resp.getWriter().println(String.format(TEMPLATE, result.toString()));
    }

    //showing statistics
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        StringBuilder result = new StringBuilder("<b>Answers statistics:</b><br>");
        for (String statisticsEntry : statistics) {
            result = result.append(statisticsEntry);
        }
        resp.getWriter().println(String.format(TEMPLATE, result.toString()));
    }
}