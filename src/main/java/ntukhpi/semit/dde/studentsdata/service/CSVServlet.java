package ntukhpi.semit.dde.studentsdata.service;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@WebServlet("/csv")
public class CSVServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String path = "/csv.jsp";
            ServletContext servletContext = getServletContext();
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(path);
            requestDispatcher.forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        doGet(request, response);
//
//        String filePath="C:\\Users\\Master\\Desktop\\1.csv";
//
//        int batchSize=20;
//
//        try{
//
//            String sql = "INSERT INTO Students (Surname, Name, Fathersname,Gender, OutlookMail, Email, Phone, Phone_1, Phone_2) VALUES (?,?,?,?,?,?,?,?,?)";
//
//            PreparedStatement statement=con.prepareStatement(sql);
//
//            BufferedReader lineReader=new BufferedReader(new FileReader(filePath));
//
//            String lineText=null;
//            int count=0;
//
//            lineReader.readLine();
//            while ((lineText=lineReader.readLine())!=null){
//                String[] data=lineText.split(",");
//
//                String id=data[0];
//                String Surname=data[1];
//                String Name=data[2];
//                String FathersName=data[3];
//                String Gender=data[4];
//                String OutlookMail=data[5];
//                String Email=data[6];
//                String Phone=data[7];
//                String Phone_1=data[8];
//                String Phone_2=data[9];
//
//                statement.setInt(1,Integer.parseInt(id));
//                statement.setString(2,Surname);
//                statement.setString(3,Name);
//                statement.setString(4,FathersName);
//                statement.setString(5,Gender);
//                statement.setString(6,OutlookMail);
//                statement.setString(7,Email);
//                statement.setString(8,Phone);
//                statement.setString(9,Phone_1);
//                statement.setString(9,Phone_2);
//
//
//                statement.addBatch();
//
//                if(count%batchSize==0){
//                    statement.executeBatch();
//                }
//            }
//            lineReader.close();
//            statement.executeBatch();
//            con.commit();
//            con.close();
//            System.out.println("Data has been inserted successfully.");
//
//        }
//        catch (Exception exception){
//            exception.printStackTrace();
//        }
    }
}
