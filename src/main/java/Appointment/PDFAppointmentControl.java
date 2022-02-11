package Appointment;

import Connection.DBControl;


import java.io.IOException;
import java.net.URL;
import java.sql.*;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import java.io.FileOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;


import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.property.TextAlignment;
import javafx.fxml.Initializable;

import java.io.FileNotFoundException;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Chau Truong Vinh Hoang
 * Controller class for Appointment and PDF export
 */

public class PDFAppointmentControl implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private static Appointment getAppointment(String appointmentID) throws SQLException {
        Statement stmt = DBControl.dbConnection.createStatement();
        String sql = "select * from appointment where appointmentID='"+appointmentID+"'";
        ResultSet rs = stmt.executeQuery(sql);
        Appointment appointment = new Appointment();
        while(rs.next()){
            appointment.setPatientID(rs.getString(2));
            appointment.setDoctorID(rs.getString(3));
            appointment.setHealthDeptName(rs.getString(4));
            appointment.setHealthDescription(rs.getString(5));
            appointment.setSessionStartTime(rs.getString(6));
            appointment.setSessionEndTime(rs.getString(7));
        }
        //System.out.println(appointment.getPatientID());
        return appointment;
    }

    /**
     * Method to create PDF based on the booked appointment
     * @param appointmentID indicates the ID which is generated after booking an appointment successfully
     * @throws SQLException that provides information on a database access error or other errors
     */
    public static void createPDFAppointment(String appointmentID) throws SQLException {
        String destinationFilePath = System.getProperty("user.dir")+"/src/main/java/Appointment/PatientAppointment.pdf";
        Appointment appointment = getAppointment(appointmentID);
        String patientName = null;
        Statement stmt = DBControl.dbConnection.createStatement();
        String sql = "select firstName, lastName from user where accountID='"+appointment.getPatientID()+"'";
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            patientName = rs.getString(1) +" "+ rs.getString(2);
        }

        String doctorName = null;
        String clinicName = null;
        Statement stmt2 = DBControl.dbConnection.createStatement();
        String sql2 = "select firstName, lastName, clinicName from doctor where doctorID='"+appointment.getDoctorID()+"'";
        ResultSet rs2 = stmt.executeQuery(sql2);
        while(rs2.next()){
            doctorName = rs2.getString(1) + " "+rs2.getString(2);
            clinicName = rs2.getString(3);
        }
        try{
            //System.out.println(appointment.getPatientID());
            PdfWriter writer = new PdfWriter(destinationFilePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            pdfDoc.addNewPage();
            Document document = new Document(pdfDoc);

            //set header Font
            Style headerStyle = new Style();
            PdfFont headerFont = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
            headerStyle.setFont(headerFont).setFontSize(16);

            //set content Font
            Style contentStyle = new Style();
            PdfFont contentFont = PdfFontFactory.createFont(FontConstants.HELVETICA);
            contentStyle.setFont(contentFont).setFontSize(12);

            Paragraph header = new Paragraph();
            header.add(new Text("EHEALTH APPOINTMENT").addStyle(headerStyle));
            header.setTextAlignment(TextAlignment.CENTER);

            Paragraph content = new Paragraph();
            content.add(new Text("Your name: "+patientName+" \n").addStyle(contentStyle));
            content.add(new Text("Doctor name: "+doctorName+" \n").addStyle(contentStyle));
            content.add(new Text("Health description: "+ appointment.getHealthDescription()+"\n").addStyle(contentStyle));
            content.add(new Text("Health department name: "+clinicName+" \n").addStyle(contentStyle));
            content.add(new Text("The appointment starts at: "+appointment.getSessionStartTime()+ " \n").addStyle(contentStyle));
            content.add(new Text("The appointment ends at: "+appointment.getSessionEndTime()+ " \n").addStyle(contentStyle));

            document.add(header);
            document.add(content);
            document.close();
            System.out.println("Patient appointment pdf is createed at: "+destinationFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main function to execute the action
     * @throws SQLException that provides information on a database access error or other errors
     */
    public static void main(String[]args) throws SQLException {
        String appointmentID ="abc1234";
        String str= System.getProperty("user.dir")+"/src/main/java/Appointment/PatientAppointment.pdf";
        System.out.println("Working Directory = " + str);
        PDFAppointmentControl.createPDFAppointment(appointmentID);
    }

}
