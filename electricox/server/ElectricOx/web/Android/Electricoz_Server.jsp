

<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.LocalDate"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Iterator"%>
<%@page import="DbConnection.DBConnection"%>
<%


DBConnection con = new DBConnection();
    String key = request.getParameter("key").trim();
    System.out.println(key);




    /////////////////////////////////////user Register///////////////////////////////////////////////////////////////////
    
  if (key.equals("userRegister")) {
        String NAME = request.getParameter("name");
        String ADDRESS = request.getParameter("address");
        String NUMBER = request.getParameter("phone");
        String EMAIL = request.getParameter("email");
        String PASSWORD = request.getParameter("pass");
        

  
        
        String insertQry = "SELECT COUNT(*) FROM `user_reg` WHERE `u_email`='" + EMAIL + "' OR `u_phone` ='" + NUMBER + "'";
        System.out.println(insertQry);
        System.out.println("helooooooo");

        Iterator it = con.getData(insertQry).iterator();
        System.out.println("heloooooooooooooooooo");
        if (it.hasNext()) {
            Vector vec = (Vector) it.next();
            int max_vid = Integer.parseInt(vec.get(0).toString());
            System.out.println(max_vid);
            
            System.out.println("heloooooooooooooooooo");
            if (max_vid == 0) {
                String qry1 = "INSERT INTO `user_reg` (`u_name`,`u_phone`,`u_address`,`u_email`)VALUES('"+NAME+"','"+NUMBER+"','"+ADDRESS+"','"+EMAIL+"');";
                String qry2 = "INSERT INTO `login`(`reg_id`,`email`,`password`,`type`) VALUES((SELECT MAX(u_id)FROM `user_reg`),'" + EMAIL + "','" + PASSWORD + "','USER')";
                System.out.println(qry1 + "\n" + qry2);

                if (con.putData(qry1) > 0 && con.putData(qry2) > 0) {

                      System.out.println("Registerd");
                    out.println("Registerd");

                } else {
                     System.out.println("Registration failed");
                    out.println("Registration failed");
                }

            } else {
                System.out.println("Already Exist");
                out.println("Already Exist");
            }
        } else {
            out.println("failed");
        }

    }
  



    
       /////////////////////////////////////cs OWNER Register///////////////////////////////////////////////////////////////////
    
  if (key.equals("CSRegister")) {
        String NAME = request.getParameter("name");
        String ADDRESS = request.getParameter("address");
        String NUMBER = request.getParameter("phone");
        String EMAIL = request.getParameter("email");
        String PASSWORD = request.getParameter("pass");
        

  
        
        String insertQry = "SELECT COUNT(*) FROM `bunk_reg` WHERE `b_email`='" + EMAIL + "' OR `b_phone` ='" + NUMBER + "'";
        System.out.println(insertQry);
        System.out.println("helooooooo");

        Iterator it = con.getData(insertQry).iterator();
        System.out.println("heloooooooooooooooooo");
        if (it.hasNext()) {
            Vector vec = (Vector) it.next();
            int max_vid = Integer.parseInt(vec.get(0).toString());
            System.out.println(max_vid);
            
            System.out.println("heloooooooooooooooooo");
            if (max_vid == 0) {
                String qry1 = "INSERT INTO `bunk_reg` (`b_name`,`b_phone`,`b_address`,`b_email`)VALUES('"+NAME+"','"+NUMBER+"','"+ADDRESS+"','"+EMAIL+"');";
                String qry2 = "INSERT INTO `login`(`reg_id`,`email`,`password`,`type`) VALUES((SELECT MAX(b_id)FROM `bunk_reg`),'" + EMAIL + "','" + PASSWORD + "','CHARGING STATION')";
                System.out.println(qry1 + "\n" + qry2);

                if (con.putData(qry1) > 0 && con.putData(qry2) > 0) {

                      System.out.println("Registerd");
                    out.println("Registerd");

                } else {
                     System.out.println("Registration failed");
                    out.println("Registration failed");
                }

            } else {
                System.out.println("Already Exist");
                out.println("Already Exist");
            }
        } else {
            out.println("failed");
        }

    }
  

///////////////////////////////////////////////////////////login////////////////////////////////////////////////////////

      if (key.trim().equals("login")) {
        String EMAIL = request.getParameter("email");
        String PASSWORD = request.getParameter("pass");

        String LoginQry = "SELECT * FROM login WHERE `email`='" + EMAIL+ "' AND PASSWORD='" + PASSWORD + "' and STATUS='1'";
        System.out.println(LoginQry);
        Iterator i = con.getData(LoginQry).iterator();

        if (i.hasNext()) {
            Vector v = (Vector) i.next();
            out.println(v.get(1) + "#" + v.get(4) + "#");
            System.out.println(v.get(1) + "#" + v.get(4));
        } else {
            out.println("failed");
        }
    }



///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        
/////////////////////////////////////////////////////view user/////////////////////////////////////////////////////////////////
    if (key.equals("viewUser")) {
        System.out.println("heloo");
        String id = request.getParameter("reg_id").trim();
        String qry = " SELECT  `user_reg`.*  ,login.`status`,login.`type` FROM `user_reg`,`login` WHERE `user_reg`.`u_id`=`login`.`reg_id` AND login.`type`='USER' ORDER BY `u_id` DESC";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                obj.put("u_id", vector.get(0).toString().trim());
                obj.put("u_name", vector.get(1).toString().trim());
                obj.put("u_phone", vector.get(2).toString().trim());
                obj.put("u_email", vector.get(4).toString().trim());
                obj.put("u_address", vector.get(3).toString().trim());
                obj.put("status", vector.get(5).toString().trim());
                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }
    

        //////////////////////////////////////////////////blockUser/////////////////////////////////////////////////////////////////
    if (key.equals("BlockRequest")) {
        String id = request.getParameter("reg_id");
        String insertQry = "UPDATE login SET status='2' WHERE `reg_id`='" + id + "'and type='USER'";

        System.out.println(insertQry);
        if (con.putData(insertQry) > 0) {
            out.println("saved");
        } else {
            out.println("failed");
        }
    }
    

//////////////////////////////////////////////////accept user//////////////////////////////////////////////////////////
    if (key.equals("AcceptRequest")) {
        String id = request.getParameter("reg_id");
        String insertQry = "UPDATE login SET status='1' WHERE `reg_id`='" + id + "'and type='USER'";

        System.out.println(insertQry);
        if (con.putData(insertQry) > 0) {
            out.println("saved");
        } else {
            out.println("failed");
        }
    }


    
/////////////////////////////////////////////////////view user/////////////////////////////////////////////////////////////////
    if (key.equals("viewOwners")) {
        System.out.println("heloo");
        //String id = request.getParameter("reg_id").trim();
        String qry = " SELECT  `bunk_reg`.* , `login`.`status`,`login`.`type` FROM `bunk_reg`,`login` WHERE `bunk_reg`.`b_id`=`login`.`reg_id` AND `login`.`type`='CHARGING STATION' ORDER BY `b_id` DESC";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                obj.put("b_id", vector.get(0).toString().trim());
                obj.put("b_name", vector.get(1).toString().trim());
                obj.put("b_phone", vector.get(2).toString().trim());
                obj.put("b_email", vector.get(4).toString().trim());
                obj.put("b_address", vector.get(3).toString().trim());
                obj.put("status", vector.get(5).toString().trim());
                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }


    

        //////////////////////////////////////////////////blockUser/////////////////////////////////////////////////////////////////
    if (key.equals("BlockEVOwner")) {
        String id = request.getParameter("reg_id");
        String insertQry = "UPDATE login SET status='2' WHERE `reg_id`='" + id + "'and type='CHARGING STATION'";

        System.out.println(insertQry);
        if (con.putData(insertQry) > 0) {
            out.println("saved");
        } else {
            out.println("failed");
        }
    }
    

//////////////////////////////////////////////////accept user//////////////////////////////////////////////////////////
    if (key.equals("AcceptEVOwner")) {
        String id = request.getParameter("reg_id");
        String insertQry = "UPDATE login SET status='1' WHERE `reg_id`='" + id + "'and type='CHARGING STATION'";

        System.out.println(insertQry);
        if (con.putData(insertQry) > 0) {
            out.println("saved");
        } else {
            out.println("failed");
        }
    }
    


    //////////////////////////////////////////////////accept user//////////////////////////////////////////////////////////
    if (key.equals("addBunk")) {
        String id = request.getParameter("id");
         String businessHours = request.getParameter("businessHours");
          String bunkName = request.getParameter("bunkName");
        
           String emergencyContact = request.getParameter("emergencyContact");
            String onSiteSupport = request.getParameter("onSiteSupport");
             String IsWifiAvailable = request.getParameter("IsWifiAvailable");
              String HasRestroomFacilities = request.getParameter("HasRestroomFacilities");
               String image = request.getParameter("image");
                String lat = request.getParameter("lat");
                 String longg = request.getParameter("long");
                  String HasWaitingArea = request.getParameter("HasWaitingArea");
                   String HasNearbyAmenities = request.getParameter("HasNearbyAmenities");
                    String ADDRESS = request.getParameter("ADDRESS");
                    
        String insertQry = "INSERT INTO `bunk`(`b_owner_id`,`bunk_name`,`bunk_hours`,`wifi_availability`,`restroom`,`waiting_area`,`nearBy_amenities`,`emergency_number`,`onsite_support`,`bunk_image`,`bunk_address`,`lat`,`longg`)VALUES('"+id+"','"+bunkName+"','"+businessHours+"','"+IsWifiAvailable+"','"+HasRestroomFacilities+"','"+HasWaitingArea+"','"+HasNearbyAmenities+"','"+emergencyContact+"','"+onSiteSupport+"','"+image+"','"+ADDRESS+"','"+lat+"','"+longg+"');";

        System.out.println(insertQry);
        if (con.putData(insertQry) > 0) {
            out.println("saved");
        } else {
            out.println("failed");
        }
    }




            /////////////////////////////view bunk Admin/////////////////////////////////////////////////////
            


  if (key.equals("viewEVStations")) {
        System.out.println("heloo");
        //String id = request.getParameter("reg_id").trim();
        String qry = " SELECT *  FROM `bunk` ORDER BY `bunk_id` DESC";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                obj.put("bunk_id", vector.get(0).toString().trim());
                obj.put("b_owner_id", vector.get(1).toString().trim());
                obj.put("bunk_name", vector.get(2).toString().trim());
                obj.put("bunk_hours", vector.get(3).toString().trim());
                obj.put("wifi_availability", vector.get(4).toString().trim());
                obj.put("restroom", vector.get(5).toString().trim());
                obj.put("waiting_area", vector.get(6).toString().trim());
                obj.put("nearBy_amenities", vector.get(7).toString().trim());
                obj.put("emergency_number", vector.get(8).toString().trim());
                obj.put("onsite_support", vector.get(9).toString().trim());
                obj.put("bunk_image", vector.get(10).toString().trim());
                obj.put("bunk_address", vector.get(11).toString().trim());
                obj.put("lat", vector.get(12).toString().trim());
                obj.put("longg", vector.get(13).toString().trim());
                obj.put("status", vector.get(14).toString().trim());
                obj.put("userType","Admin");
                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }
            



    //////////////////////////////////////////////////blockUser//////////////////////////////////////////////////////////////
    
    if (key.equals("blockbunk")) {
        String id = request.getParameter("reg_id");
        String insertQry = "UPDATE `bunk` SET`status` ='2' WHERE `bunk_id`='"+id+"'";

        System.out.println(insertQry);
        if (con.putData(insertQry) > 0) {
            out.println("saved");
        } else {
            out.println("failed");
        }
    }
    

    //////////////////////////////////////////////////accept user//////////////////////////////////////////////////////////
    
    if (key.equals("acceptbunk")) {
        String id = request.getParameter("reg_id");
        String insertQry = "UPDATE `bunk` SET`status` ='1' WHERE `bunk_id`='"+id+"'";

        System.out.println(insertQry);
        if (con.putData(insertQry) > 0) {
            out.println("saved");
        } else {
            out.println("failed");
        }
    }
    




          /////////////////////////////view bunk Owner/////////////////////////////////////////////////////
            


  if (key.equals("viewEVStationsOwner")) {
        System.out.println("heloo");
        String id = request.getParameter("reg_id").trim();
        String qry = " SELECT *  FROM `bunk` WHERE `b_owner_id`='"+id+"' ORDER BY `bunk_id` DESC";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                obj.put("bunk_id", vector.get(0).toString().trim());
                obj.put("b_owner_id", vector.get(1).toString().trim());
                obj.put("bunk_name", vector.get(2).toString().trim());
                obj.put("bunk_hours", vector.get(3).toString().trim());
                obj.put("wifi_availability", vector.get(4).toString().trim());
                obj.put("restroom", vector.get(5).toString().trim());
                obj.put("waiting_area", vector.get(6).toString().trim());
                obj.put("nearBy_amenities", vector.get(7).toString().trim());
                obj.put("emergency_number", vector.get(8).toString().trim());
                obj.put("onsite_support", vector.get(9).toString().trim());
                obj.put("bunk_image", vector.get(10).toString().trim());
                obj.put("bunk_address", vector.get(11).toString().trim());
                obj.put("lat", vector.get(12).toString().trim());
                obj.put("longg", vector.get(13).toString().trim());
                obj.put("status", vector.get(14).toString().trim());
                obj.put("userType","Owner");
                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }
            



         /////////////////////////////view bunk User/////////////////////////////////////////////////////
            


  if (key.equals("viewEVStationsUser")) {
        System.out.println("heloo");
        String qry = " SELECT *  FROM `bunk` WHERE status='1' ORDER BY `bunk_id` DESC";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                obj.put("bunk_id", vector.get(0).toString().trim());
                obj.put("b_owner_id", vector.get(1).toString().trim());
                obj.put("bunk_name", vector.get(2).toString().trim());
                obj.put("bunk_hours", vector.get(3).toString().trim());
                obj.put("wifi_availability", vector.get(4).toString().trim());
                obj.put("restroom", vector.get(5).toString().trim());
                obj.put("waiting_area", vector.get(6).toString().trim());
                obj.put("nearBy_amenities", vector.get(7).toString().trim());
                obj.put("emergency_number", vector.get(8).toString().trim());
                obj.put("onsite_support", vector.get(9).toString().trim());
                obj.put("bunk_image", vector.get(10).toString().trim());
                obj.put("bunk_address", vector.get(11).toString().trim());
                obj.put("lat", vector.get(12).toString().trim());
                obj.put("longg", vector.get(13).toString().trim());
                obj.put("status", vector.get(14).toString().trim());
                obj.put("userType","User");
                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }
            

            
  /////////////////////////////view bunk User  search   / ////////////////////////////////////////////////////
            


  if (key.equals("viewEVStationsUserSearch")) {
        System.out.println("heloo");
        
        String searchText = request.getParameter("serchValue").trim();
         
        String text = "%" + searchText + "%";
        
        String qry = "SELECT * FROM `bunk` WHERE `bunk_address`LIKE  '"+text+"' and status='1' ORDER BY `bunk_id` DESC";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                obj.put("bunk_id", vector.get(0).toString().trim());
                obj.put("b_id", vector.get(1).toString().trim());
                obj.put("bunk_name", vector.get(2).toString().trim());
                obj.put("bunk_hours", vector.get(3).toString().trim());
                obj.put("bunk_charging_rate", vector.get(4).toString().trim());
                obj.put("wifi_availability", vector.get(5).toString().trim());
                obj.put("restroom", vector.get(6).toString().trim());
                obj.put("waiting_area", vector.get(7).toString().trim());
                obj.put("nearBy_amenities", vector.get(8).toString().trim());
                obj.put("emergency_number", vector.get(9).toString().trim());
                obj.put("onsite_support", vector.get(10).toString().trim());
                obj.put("bunk_image", vector.get(11).toString().trim());
                obj.put("bunk_address", vector.get(12).toString().trim());
                obj.put("lat", vector.get(13).toString().trim());
                obj.put("longg", vector.get(14).toString().trim());
                obj.put("status", vector.get(15).toString().trim());
                obj.put("userType","User");
                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }
  



///////////////////////////////////////////////////////////////////////////////////////////////
            
    if (key.equals("getSlotStatus")) {
        System.out.println("heloo");
        String id = request.getParameter("bunk_id");
        String qry = "SELECT `booking_status`, `slot_number` , `bunk_id`,`booking_time` FROM `slot_booking` WHERE bunk_id ='"+id+"'";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                 obj.put("slot_number", vector.get(1).toString().trim());
                 obj.put("booking_status", vector.get(0).toString().trim());
                 obj.put("bunk_id", vector.get(2).toString().trim());
                 obj.put("booking_time", vector.get(3).toString().trim());

                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if (key.equals("addParkingRequest")) {
        String user_id = request.getParameter("login_id");
        String bunk_id = request.getParameter("bunk_id");
        String SLOTNUMBER = request.getParameter("SLOTNUMBER");
        String DRIVINGLICENS = request.getParameter("DRIVINGLICENS");
        String PARKTIME = request.getParameter("PARKTIME");
        String owner_id = request.getParameter("owner_id");
        String Status="requested";
        String QrStatus="not scanned";
//        String PaymentStatus="not payed";

        String insertQry = "INSERT INTO `slot_booking`(`bunk_id`,`user_id`,`licenceNumber`,`slot_number`,`booking_time`,`booking_status`,`qr_status`, b_id)VALUES('"+bunk_id+"','"+user_id+"','"+DRIVINGLICENS+"','"+SLOTNUMBER+"','"+PARKTIME+"','"+Status+"','"+QrStatus+"', '"+owner_id+"');";
        System.out.println(insertQry);
        if (con.putData(insertQry) > 0) {

            out.println("saved");
        } else {
            out.println("failed");
        }

    }
        
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (key.equals("getParkingRequest")) {
        System.out.println("heloo");
        String USER_ID = request.getParameter("login_id");
        String SlotId = request.getParameter("SLOTNUMBER");
        String bunk_id = request.getParameter("bunk_id");
        
        String qry = "SELECT `slot_booking`.`booking_status` FROM `slot_booking` WHERE `slot_booking`.`user_id`='"+USER_ID+"' AND `slot_booking`.`slot_number`='"+SlotId+"' AND `slot_booking`.`bunk_id`='"+bunk_id+"'";
        System.out.println("qry=" + qry);
        Iterator i = con.getData(qry).iterator();

        if (i.hasNext()) {
            Vector v = (Vector) i.next();
            out.println(v.get(0));
            System.out.println(v.get(0));
        } else {
            out.println("failed");
        }
    }
        
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

if (key.equals("RequeststochargingStation")) {
        System.out.println("heloo");

        String reg_id = request.getParameter("id");
    
        String qry = "SELECT `slot_booking`.* ,`user_reg`.* , `bunk_reg`.* , `bunk`.`b_owner_id` FROM `slot_booking`,`user_reg` , `bunk_reg` , `bunk`  WHERE `slot_booking`.`user_id`=`user_reg`.`u_id`  AND `bunk`.`b_owner_id`= `bunk_reg`.`b_id` AND `slot_booking`.`b_id`=`bunk_reg`.`b_id` AND `bunk`.`b_owner_id`='"+reg_id+"'  GROUP BY `slot_booking`.`booking_id` DESC ";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                
               obj.put("booking_id", vector.get(0).toString().trim()); 
                obj.put("bunk_id", vector.get(1).toString().trim());
                 obj.put("u_id", vector.get(2).toString().trim()); 
                obj.put("licenceNumber", vector.get(3).toString().trim());
                obj.put("slot_number", vector.get(4).toString().trim());
                obj.put("booking_time", vector.get(5).toString().trim());
                obj.put("booking_status", vector.get(6).toString().trim());
                 obj.put("qr_status", vector.get(7).toString().trim());

                obj.put("u_name", vector.get(10).toString().trim());              
                obj.put("u_phone", vector.get(11).toString().trim());
                obj.put("u_address", vector.get(12).toString().trim());
                obj.put("u_email", vector.get(13).toString().trim());       
                obj.put("userType", "viewchargingStation");
            
            
                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
        
}
        
   //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        
        
  if(key.equals("addQrCodeeeee")) {
    
        String user_id = request.getParameter("user_id");
        String reqId = request.getParameter("booking_id");
        String QrCode = request.getParameter("QrCode");
        
        String insertQry   = "INSERT INTO `qr_code` (`user_id`,`booking_id`,`qr_code`)VALUES('"+user_id+"' ,'"+reqId+"', '"+QrCode+"')";
        String insertQry1 = "UPDATE `slot_booking` SET `booking_status`='Accepted' WHERE `booking_id`='"+reqId+"'";
      System.out.println(insertQry);
      System.out.println(insertQry1);
        if (con.putData(insertQry) > 0 &&con.putData(insertQry1) > 0  )
        {
            out.println("saved");
        } else {
            out.println("failed");
        }

    }
  


///////////////////////////////////////////////////////REquests to user///////////////////////////////////////////////////////////////////////
   

        if (key.equals("viewUserRequests")) {
        System.out.println("heloo");
        String user_id= request.getParameter("id");
    
        String qry = "SELECT `slot_booking`.* ,`user_reg`.* FROM `slot_booking`,`user_reg` WHERE `slot_booking`.`user_id`=`user_reg`.`u_id` AND `slot_booking`.`user_id`='"+user_id+"' GROUP BY `slot_booking`.`booking_id` DESC";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                
                 obj.put("booking_id", vector.get(0).toString().trim()); 
                 obj.put("bunk_id", vector.get(1).toString().trim());
                 obj.put("u_id", vector.get(2).toString().trim()); 
                 obj.put("licenceNumber", vector.get(3).toString().trim());
                 obj.put("slot_number", vector.get(4).toString().trim());
                 obj.put("booking_time", vector.get(5).toString().trim());
                 obj.put("booking_status", vector.get(6).toString().trim());
                 obj.put("qr_status", vector.get(7).toString().trim());
       
                obj.put("b_id", vector.get(8).toString().trim());
                 obj.put("u_name", vector.get(9).toString().trim());              
                obj.put("u_phone", vector.get(10).toString().trim());
                obj.put("u_address", vector.get(12).toString().trim());
                obj.put("u_email", vector.get(13).toString().trim());       
                obj.put("userType", "viewUser");
            
            
                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }
        

////////////////////////////////getQr////////////////////////////////////////////////////////////////////////////////////////

    if (key.equals("getQr")) {
        System.out.println("heloo");
        String id = request.getParameter("id");
        String qry = "SELECT * FROM `qr_code` WHERE `booking_id`='"+id+"'";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                obj.put("QR_code", vector.get(3).toString().trim());   
                obj.put("booking_id", vector.get(2).toString().trim()); 
                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }
        
/////////////////////////////////////////////////////////////////////////////////////////////////

   if (key.trim().equals("getPrice")) {
         String id = request.getParameter("bunkId");
        String LoginQry = "SELECT `bunk`.`bunk_charging_rate` FROM `bunk` WHERE `bunk_id`='"+id+"'";
        System.out.println(LoginQry);
        Iterator i = con.getData(LoginQry).iterator();

        if (i.hasNext()) {
            Vector v = (Vector) i.next();
            out.println(v.get(0) );
            System.out.println(v.get(0) );
        } else {
            out.println("failed");
        }
    }
   




if (key.equals("updatePass")) {
        String id = request.getParameter("id");
        String insertQry = "UPDATE `slot_booking` SET  `qr_status`='Scanned' WHERE `booking_id`='"+id+"'";
           String insertQry1 = "UPDATE `slot_booking` SET  booking_status='Available' WHERE `booking_id`='"+id+"'";

        System.out.println(insertQry);
        if (con.putData(insertQry) > 0 && con.putData(insertQry1) > 0) {
            out.println("saved");
        } else {
            out.println("failed");
        }
    }




//////////////////////////////////////////////////////////////////add feedback////////////////////////////////////////////

                
    if (key.equals("addFeedBack")) {    
        
             String user_id = request.getParameter("uid");
             String booking_id = request.getParameter("booking_id");
              String subject = request.getParameter("subject");
             String rating = request.getParameter("rating");
             String description = request.getParameter("description");
     
             String b_id = request.getParameter("b_id");
   
        
       String insertQry = "INSERT INTO `feedback`(`user_id`,`booking_id`,`subject`,`description`,`rating` ,b_id)VALUES('"+user_id+"','"+booking_id+"','"+subject+"','"+description+"','"+rating+"' ,'"+b_id+"')";
       System.out.println(insertQry);
        if (con.putData(insertQry) > 0) {

            out.println("saved");
        } else {
            out.println("failed");
        }

    }
  


// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
         
    if (key.equals("addFeedBack")) {    
        
             String user_id = request.getParameter("uid");
             String booking_id = request.getParameter("booking_id");
              String subject = request.getParameter("subject");
             String rating = request.getParameter("rating");
             String description = request.getParameter("description");
     
        
       String insertQry = "INSERT INTO `feedback`(`user_id`,`booking_id`,`subject`,`description`,`rating`)VALUES('"+user_id+"','"+booking_id+"','"+subject+"','"+description+"','"+rating+"')";
       System.out.println(insertQry);
        if (con.putData(insertQry) > 0) {

            out.println("saved");
        } else {
            out.println("failed");
        }

    }
/////////////////////////////////////////////////////getFeedback///////////////////////////////////////////////////////////////
      
if (key.equals("getFeedback")) 
{
        System.out.println("heloo");
        String user_id = request.getParameter("uid");
        String qry = "SELECT `feedback`.* ,`user_reg`.`u_name`,`slot_booking`.`slot_number` FROM `feedback`,`user_reg`,`slot_booking` WHERE `feedback`.`user_id`=`user_reg`.`u_id` AND  `feedback`.`booking_id`=`slot_booking`.`booking_id`  AND `slot_booking`.`b_id`='"+user_id+"' GROUP BY `feedback`.`feedback_id` DESC ";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                obj.put("u_name", vector.get(7).toString().trim());
                obj.put("slot_number", vector.get(8).toString().trim());
                obj.put("description", vector.get(4).toString().trim());
                obj.put("rating", vector.get(5).toString().trim());
                obj.put("subject", vector.get(3).toString().trim());
                obj.put("userType", "User");
                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
 }
        

/////////////////////////////////////////////////////getFeedback/////////////////////////////////////////////////
        if (key.equals("getFeedbackAdmin")) {
        System.out.println("heloo");
        String qry = "SELECT `feedback`.* ,`user_reg`.`u_name`,`slot_booking`.`slot_number` , `bunk_reg`.* FROM `feedback`,`user_reg`,`slot_booking` ,`bunk_reg`  WHERE `feedback`.`user_id`=`user_reg`.`u_id` AND  `feedback`.`booking_id`=`slot_booking`.`booking_id` AND `feedback`.`b_id` = `bunk_reg`.`b_id`   GROUP BY `feedback`.`feedback_id` DESC";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                obj.put("u_name", vector.get(7).toString().trim());
                obj.put("slot_number", vector.get(8).toString().trim());
                obj.put("description", vector.get(4).toString().trim());
                obj.put("rating", vector.get(5).toString().trim());
                obj.put("subject", vector.get(3).toString().trim());
                obj.put("b_id", vector.get(6).toString().trim());
                obj.put("b_name", vector.get(10).toString().trim());
                obj.put("b_phone", vector.get(11).toString().trim());
                obj.put("b_email", vector.get(13).toString().trim());
                obj.put("b_address", vector.get(12).toString().trim());
               obj.put("userType", "Admin");
            
                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }
        






%>
