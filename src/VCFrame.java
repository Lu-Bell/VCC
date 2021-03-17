import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.sql.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.lang.Object;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class VCFrame implements ActionListener {

    ClientFrame cF = new ClientFrame();
    OwnerFrame oF = new OwnerFrame();
    UserFrame uF = new UserFrame();
    JFrame controllerView = new JFrame();
    JButton accept = new JButton("Accept");
    JButton reject = new JButton("Reject");
    JButton newEntry = new JButton("New Entry");
    JButton assign = new JButton("Assign");
    JButton ok = new JButton("OK");
    JButton waitTime = new JButton("Calculate Wait Time");
    JLabel currentWait = new JLabel("Current WaitTime: ");
    JLabel clientId;
    JLabel duration;
    JLabel deadline;
    JLabel ownerId;
    JLabel licensePlate;
    JLabel residency;
    JLabel currentTime;
    java.util.Date date = new java.util.Date();
    JFrame jobRejected = new JFrame();
    JLabel sorry = new JLabel("     Sorry, this job has been rejected.");
    JFrame jobAccepted = new JFrame();
    JLabel congrats = new JLabel("     Congrats, this job has been accepted.");
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy : hh:mm:ss");
    Date curDate = new Date();
    String strDate = sdf.format(curDate);
    Queue<Integer> jobDuration = new LinkedList<>();
    Queue<String> vehicles = new LinkedList<>();
    static Connection connection = null;
    static String url = "jdbc:mysql://localhost:3306/vcrts?useTimezone=true&serverTimezone=UTC";
    static String username = "root";
    static String password = "samantha1999";
    boolean type = uF.getUserType();
    

    VCFrame() {
    		
    	//hides all the frames from view
        cF.hide();
        oF.hide();
        uF.hide();
        controllerView.setLayout(new FlowLayout());
        //Client
      
        	
        
        if (type) {
            clientId = new JLabel("Client Id: " + cF.getClientId());
            duration = new JLabel("Duration: " + cF.getDuration());
            deadline = new JLabel("Deadline: " + cF.getDeadline());
            controllerView.add(clientId);
            controllerView.add(duration);
            controllerView.add(deadline);
        } //Owner
        else {
            ownerId = new JLabel("Owner Id: " + oF.getOwnerID());
            licensePlate = new JLabel("License Plate: " + oF.getLicensePlate());
            residency = new JLabel("Residency: " + oF.getResidency());
            controllerView.add(ownerId);
            controllerView.add(licensePlate);
            controllerView.add(residency);
        }
        controllerView.add(accept);
        accept.addActionListener(this);

        controllerView.add(reject);
        reject.addActionListener(this);
        

        controllerView.setSize(500, 500);
        controllerView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controllerView.setVisible(true);
        
        //JOB ACCEPTED VIEW
        jobAccepted.setLayout(new FlowLayout());
        jobAccepted.add(congrats);
        
        //NEW ENTRY BUTTON
        newEntry.addActionListener(e -> {
        	//EVERYTIME THE NEW ENTRY BUTTON IS PRESSED THEN GO BACK TO THE USER FRAME
        	System.out.println("button pressed");
        	new UserFrame();
        });
        jobAccepted.add(newEntry);
        
        //ASSIGN BUTTON
        assign.addActionListener(e -> {
        	Queue<Integer> job = new LinkedList<>(); // QUEUE FOR JOBS
        	Queue<Integer> v = new LinkedList<>(); // QUEUE FOR VEHICLES
        	job.add(1);
        	job.add(32);
        	job.add(6);
        	job.add(8);
        	job.add(5);	
        	
        	if(oF.getOwnerID() != 0 && oF.getLicensePlate()!= null && oF.getResidency() != 0) {
        		((LinkedList<Integer>) v).push(Integer.parseInt(oF.getLicensePlate()));//PUSH THE VEHICLE ONTO THE VEHICLE QUEUE
        		System.out.println(v);
        	}
        	
        	System.out.println("Job "+job.peek()+" has been assigned to vehicle "+v.peek());
        	job.remove();
        	v.remove();
        	System.out.println("The Next Job is "+job.peek());
        	
        	
        });
        jobAccepted.add(assign);
        jobAccepted.add(ok);
        ok.addActionListener(e -> {
        	   jobAccepted.dispose();  
        });
        jobAccepted.add(waitTime);
        //  jobAccepted.add(currentWait);
        waitTime.addActionListener(this);
        jobAccepted.setSize(500, 500);
        jobAccepted.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jobRejected.setLayout(new FlowLayout());
        jobRejected.add(sorry);
        jobRejected.setSize(500, 500);
        jobRejected.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

    }
    // Writes to File

    public void calculateJob(ActionEvent e) {
    
   
     if (e.getActionCommand() == accept.getActionCommand()) {
        try {
            if (type) {
                try {
                    File clientWriter = new File("client.txt");
                    if (clientWriter.createNewFile()) {
                        System.out.println("File created: " + clientWriter.getName());
                    } else {
                        System.out.println("File already exists.");
                    }
                } catch (IOException v) {
                    System.out.println("An error occurred.");
                    v.printStackTrace();
                }
                FileWriter clientWriter = new FileWriter("client.txt");

                clientWriter.write("Client ID: " + cF.getClientId());
                clientWriter.write("\nDuration: " + cF.getDuration());
                clientWriter.write("\nDead-line: " + cF.getDeadline());
                clientWriter.write("\n Current Time: " + strDate);
                jobDuration.add(cF.getClientId());
                jobDuration.add(cF.getDuration());
                jobDuration.add(cF.getDeadline());
                clientWriter.close();
                controllerView.dispose();
            } else {
                try {
                    File ownerWriter = new File("owner.txt");
                    if (ownerWriter.createNewFile()) {
                        System.out.println("File created: " + ownerWriter.getName());
                    } else {
                        System.out.println("File already exists.");
                    }
                } catch (IOException v) {
                    System.out.println("An error occurred.");
                    v.printStackTrace();
                }
                FileWriter ownerWriter = new FileWriter("owner.txt");
                ownerWriter.write("Owner ID: " + oF.getOwnerID());
                ownerWriter.write("\nLicense Plate: " + oF.getLicensePlate());
                ownerWriter.write("\n Residency: " + oF.getResidency());
                ownerWriter.write("\n Current Time: " + strDate);
                vehicles.add(oF.getLicensePlate());
            }
        } catch (IOException v) {
            System.out.println("An error occurred.");
            v.printStackTrace();
        }
    } else if (e.getActionCommand() == reject.getActionCommand()) {
         System.out.println("Data Was Rejected");
         controllerView.dispose();
    }
    }
    

    public int calculateWaitTime() {
        int currentWaitTime = 0;
        for (int i = 0; i < jobDuration.size(); i++) {
            currentWaitTime += jobDuration.peek();
            jobDuration.remove();
        }
        return currentWaitTime;
    }

    public void wt(ActionEvent e) {

        if (e.getActionCommand() == ok.getActionCommand()) {
            calculateWaitTime();
        }

    }

// Writes to database
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand() == accept.getActionCommand()) {

            String clientsql = "INSERT INTO client (ClientId, Duration, Deadline, TimeStamp) VALUES (?,?,?,?)";
            String ownersql = "INSERT INTO owner (OwnerId, LicensePlate, Residency, TimeStamp) VALUES (?,?,?,?)";
            jobAccepted.setVisible(true);
            try {
                connection = DriverManager.getConnection(url, username, password);
                if (type) {
                    PreparedStatement pstmt = connection.prepareStatement(clientsql);
                    pstmt.setInt(1, cF.getClientId());
                    pstmt.setInt(2, cF.getDuration());
                    pstmt.setInt(3, cF.getDeadline());
                    pstmt.setString(4, strDate);
                    int row = pstmt.executeUpdate();
                    System.out.println("Data Successfully Added");
                    System.out.println(row);
                    connection.close();
                } else {
                    PreparedStatement pstmt = connection.prepareStatement(ownersql);
                    pstmt.setInt(1, oF.getOwnerID());
                    pstmt.setString(2, oF.getLicensePlate());
                    pstmt.setInt(3, oF.getResidency());
                    pstmt.setString(4, strDate);
                    int row = pstmt.executeUpdate();
                    System.out.println("Data Successfully Added");
                    System.out.println(row);
                    connection.close();
                }
            } catch (SQLException f) {
                f.getMessage();
            }

        } else if (e.getActionCommand() == reject.getActionCommand()) {
            jobRejected.setVisible(true);
            System.out.println("Data Was Rejected");

        }
    }

}