package Controllers.wms;

import Models.global.sessionModel;
import Models.wms.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by georgoud on 18/9/2014.
 */
@Path("wmsapi")
public class wmsAPI {

    @Context
    private UriInfo context;

    wmsSQL sql;
    wmsReporting wrep;

    public wmsAPI() {
        sql = new wmsSQL();
        wrep = new wmsReporting();
    }


    /**
     * Enters a new bin to the system
     *
     * @param bin
     * @param response
     * @return
     * @throws SQLException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("registerBin/{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String RegisterBin(wmsBin bin, @PathParam("applicationID") String applicationID, @Context HttpServletResponse response, @Context HttpServletRequest req, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException {
        int cnt;
        String result = "";
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {

             cnt = sql.registerBin(bin, applicationID);

            result = Integer.toString(cnt);
            response.setHeader("result", result);
        }
        return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("registerBinSensor/{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String registerBinSensor(wmsBinSensor bs, @PathParam("applicationID") String applicationID, @Context HttpServletResponse response , @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException {
        int cnt;
        String result = "";
        String key = sql.findSession(userName, session);
        if (key != null&& key.equals(session))
        {
            wmsBinSensor testSens;
            if( !sql.getBinSensorByDeviceInfoParam("hwID", bs.hwID).isEmpty())
             {
                cnt=-1; //--bin exists already
            }
            else
                {
                cnt = sql.registerBinSensor(bs, applicationID);
                }
                result = Integer.toString(cnt);
                response.setHeader("result", result);
        }
        return result;
    }

    /**
     * -----------------update data for a bin such as moving to a new bin, etc.
     *
     * @param bin
     * @param response
     * @return
     * @throws SQLException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("updateBin")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String updateBin(wmsBin bin, @Context HttpServletResponse response, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException {
        String result = "";
        String key = sql.findSession(userName, session);
        if (key != null&& key.equals(session)) {
            int cnt = sql.updateBin(bin);
            result = Integer.toString(cnt);
            response.setHeader("result", result);
        }
        return result;
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("updateBinSensor")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String updateBinSensor(wmsBinSensor bs, @Context HttpServletResponse response, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException
    {
        String result = "";
        String key = sql.findSession(userName, session);
        if (key != null&& key.equals(session))
        {
            int cnt = sql.updateBinSensor(bs);
            result = Integer.toString(cnt);
            response.setHeader("result", result);
        }
        return result;
    }


    @GET
    @Path("deleteSensorBin/{binSensorID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String deleteSensorBin(@PathParam("binSensorID") String binSensorID, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException {
        String result = "";
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            int cnt = sql.deleteSensorBin(binSensorID);
            result = Integer.toString(cnt);
        }
        return result;
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("registerVehicle/{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String registerVehicle(wmsVehicle vehicle,@PathParam("applicationID") String applicationID, @Context HttpServletResponse response, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException
        {
        String result = "";
            String key = sql.findSession(userName,session);
            if (key!=null && key.equals(session)) {
                int cnt = sql.registerVehicle(vehicle, applicationID);
                result = Integer.toString(cnt);
                response.setHeader("result", result);
            }
        return result;
    }


    @GET
    @Path("getBinZone/{zoneID}&{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsBin> getBinZone(@PathParam("zoneID") String zoneID, @PathParam("applicationID") String applicationID, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException {
        ArrayList<wmsBin> binlist=null;
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            binlist = sql.getBinZone(zoneID, applicationID);
        }
        return binlist;
    }

    @GET
    @Path("getBinsByMaterial/{zoneID}&{applicationID}&{wasteMaterial}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsBin> getBinsByMaterial(@PathParam("zoneID") String zoneID, @PathParam("applicationID") String applicationID,@PathParam("wasteMaterial") String wasteMaterial, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException
    {
        ArrayList<wmsBin> binlist=null;
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            binlist = sql.getBinsByMaterial(zoneID, applicationID, wasteMaterial);
        }
        return binlist;
    }

    @GET
    @Path("getBinSensors")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
     public ArrayList<wmsBinSensor>  getAllBinSensors(@HeaderParam("userName") String userName,@HeaderParam("session") String session) throws IOException, SQLException {

        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            ArrayList<wmsBinSensor> bslist = sql.getAllBinSensors();
            return bslist;
        }
        else
            return null;
    }

    @GET
    @Path("getAllBins")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsBin>  getAllBins(@HeaderParam("userName") String userName,@HeaderParam("session") String session) throws IOException, SQLException {

        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            ArrayList<wmsBin> bslist = sql.getAllBins();
            return bslist;
        }
        else
            return null;
    }



    /**
     * this method should be called first: get all zones. One application per customer
     *
     * @param applicationID
     * @return
     * @throws SQLException
     */
    @GET
    @Path("getAllZones/{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsZone> getAllZones( @PathParam("applicationID") String applicationID, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException {
        ArrayList<wmsZone> zoneList=null;
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session))
        {
                zoneList = sql.getZones(applicationID);
        }
        return zoneList;
    }

    @GET
    @Path("deleteZone/{zoneID}&{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String deleteZone(@PathParam("zoneID") String zoneID,@PathParam("applicationID") String applicationID,  @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException
    {
        String result = "";
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            int cnt = sql.deleteZone(zoneID,applicationID);
            result = Integer.toString(cnt);
        }
        return result;
    }



    /**
     * Gets all vehicles per zone
     *
     * @param zoneID
     * @return
     * @throws SQLException
     */
    @GET
    @Path("getZoneVehicle/{zoneID}&{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsVehicle> getZoneVehicle(@PathParam("zoneID") String zoneID, @PathParam("applicationID") String applicationID, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException {
        ArrayList<wmsVehicle> vehicleList=null;
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {

            vehicleList = sql.getZoneVehicle(zoneID, applicationID);
        }
        return vehicleList;
    }

    /**
     * ---get all alarms from an application
     *
     * @param applicationID
     * @return
     * @throws SQLException
     */
    @GET
    @Path("getAlarms/{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsReading> getAlarms(@PathParam("applicationID") String applicationID, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException {
        ArrayList<wmsReading> alarmList=null;
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            alarmList = wrep.getAlarms(applicationID );
        }

        return alarmList;
    }

    /**
     * ------if an alarm is handled, admin deletes it
     *
     * @param alarmID
     * @return
     * @throws SQLException
     */
    @GET
    @Path("deleteAlarm/{alarmID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String deleteAlarm(@PathParam("alarmID") String alarmID, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException {
        String result = "";
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            int cnt = sql.deleteAlarm(alarmID);
            result = Integer.toString(cnt);
        }
        return result;
    }

    /**
     * deletes a sensors and associated sensor
     *
     * @param resourceID
     * @return
     * @throws SQLException
     */

    @GET
    @Path("deleteBin/{resourceID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String deleteBin(@PathParam("resourceID") String resourceID, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException {
        String result = "";
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            int cnt = sql.deleteBin(resourceID);
            result = Integer.toString(cnt);
        }
        return result;
    }

    @GET
    @Path("deleteVehicle/{vehicleID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String deleteVehicle(@PathParam("vehicleID") String vehicleID, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException {
        String result = "";
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            /**     NOT IMPLEMENTED YET */
            /**int cnt = sql.deleteVehicle(vehicleID);
            result = Integer.toString(cnt);*/
        }
        return result;
    }


    /**
     * ---get vehicle position
     *
     * @param position
     * @param response
     * @return
     * @throws SQLException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("postPosition")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String postVehiclePosition(wmsPosition position, @Context HttpServletResponse response, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException {
        String result = "";
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            int cnt = sql.postVehiclePosition(position);
            result = Integer.toString(cnt);
            response.setHeader("result", result);
        }
        return result;
    }

    @GET
    @Path("getBinByDeviceInfoParam/{devParam}&{devParamValue}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsBin> getBinByDeviceInfoParam(@PathParam("devParam") String devParam, @PathParam("devParamValue") String devParamValue, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException {
        ArrayList<wmsBin> binlist=null;
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            binlist = sql.getBinByDeviceInfoParam(devParam, devParamValue);
        }
        return binlist;
    }
    @GET
    @Path("getBinSensorByDeviceInfoParam/{devParam}&{devParamValue}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsBinSensor> getBinSensorByDeviceInfoParam(@PathParam("devParam") String devParam, @PathParam("devParamValue") String devParamValue, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException {
        ArrayList<wmsBinSensor> binsenslist=null;
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            binsenslist = sql.getBinSensorByDeviceInfoParam(devParam, devParamValue);
        }
        return binsenslist;
    }


    @GET
    @Path("getBinByID/{binID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public wmsBin getBinByID(@PathParam("binID") String binID, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException {
        wmsBin bin = null;
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            bin = sql.getBinByID(binID);
        }
        return bin;
    }

    @GET
    @Path("getBinByIDForQR/{binID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String getBinByIDForQR(@PathParam("binID") String binID, @Context HttpServletResponse response) throws SQLException, IOException {
        String result;
        wmsBin bin = null;
        wmsReading wmr = new wmsReading();
        bin = sql.getBinByID(binID);
        if (bin==null)
            return "Not found"; //if bin id is invalid;
        //--based on the bin, construct a reading object which will contains the alarm
        wmr.binID=bin.binID;
        wmr.measurement="0";
        wmr.alarmMessage="Πρόβλημα στον κάδο από QR";
        wmr.isAlarm="1";
        wmr.binTypeID=bin.binTypeID;
        wmr.binTypeName="0";
        wmr.latitude = bin.latitude;
        wmr.longitude=bin.longitude;
        wmr.hwid = bin.hwid;

        int cnt = sql.insertNewReading(wmr);
        result = Integer.toString(cnt);
        response.setHeader("result", result);


        return  result;
    }
    @GET
    @Path("getBinSensorApplication/{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsBinSensor>  getBinSensorApplication( @PathParam("applicationID") String applicationID, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException
    {ArrayList<wmsBinSensor> binsenslist=null;
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            binsenslist = sql.getBinSensorApplication(applicationID);
        }
        return binsenslist;

    }


    @GET
    @Path("getVehicleZone/{zoneID}&{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsVehicle> getVehicleZone(@PathParam("zoneID") String zoneID, @PathParam("applicationID") String applicationID, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException {
        ArrayList<wmsVehicle> vehlist=null;
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            vehlist = sql.getVehicleZone(zoneID, applicationID);
        }
        return vehlist;
    }

    @GET
    @Path("getPositionHistory/{vehicleID}&{applicationID},&{fromDate}&{toDate} ")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsPosition> getPositionHistory(@PathParam("vehicleID") String vehicleID, @PathParam("applicationID") String ApplicationID, @PathParam("fromDate") String fromDate, @PathParam("toDate") String toDate , @HeaderParam("userName") String userName, @HeaderParam("session") String session)  throws SQLException, IOException {
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            ArrayList<wmsPosition> positionList = sql.getPositionHistory(vehicleID, ApplicationID, fromDate, toDate);
            return positionList;
        }
        else
            return null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("insertNewReading")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String insertNewReading(wmsReading wmr, @Context HttpServletResponse response, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException {
        String result = "";
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            int cnt = sql.insertNewReading(wmr);
            result = Integer.toString(cnt);
            response.setHeader("result", result);
        }
        return result;
    }

    @GET
    @Path("getBinSuperTypes/{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsBinSuperType> getBinSuperTypes(@PathParam("applicationID") String applicationID, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException {
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            ArrayList<wmsBinSuperType> bintypeList = sql.getBinSuperTypes(applicationID);
            return bintypeList;
        }
        else
            return null;
    }

    @GET
    @Path("getBinTypes/{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsBinType> getBinTypes(@PathParam("applicationID") String applicationID, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException {
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            ArrayList<wmsBinType> bintypeList = sql.getBinTypes(applicationID);
            return bintypeList;
        }
        else
            return null;
    }

    @GET
    @Path("getBinTypeByID/{applicationID}&{binTypeID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public wmsBinType getBinTypeByID(@PathParam("applicationID") String applicationID,@PathParam("binTypeID") String binTypeID, @HeaderParam("userName") String userName, @HeaderParam("session") String session) throws SQLException, IOException {
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session))
        {
           wmsBinType bintype = sql.getBinTypeByID(applicationID, binTypeID);
            return bintype;
        }
        else
            return null;
    }

    @GET
    @Path("getBinSuperTypeByID/{applicationID}&{binSuperTypeID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public wmsBinSuperType getBinSuperTypeByID(@PathParam("applicationID") String applicationID,@PathParam("binSuperTypeID") String binSuperTypeID, @HeaderParam("userName") String userName, @HeaderParam("session") String session) throws SQLException, IOException {
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session))
        {
            wmsBinSuperType binSupertype = sql.getBinSuperTypeByID(applicationID, binSuperTypeID);
            return binSupertype;
        }
        else
            return null;
    }

    @GET
    @Path("getBinStatusTypes/{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsBinStatusType> getBinStatusTypes(@PathParam("applicationID") String applicationID, @HeaderParam("userName") String userName,@HeaderParam("session") String session)throws SQLException, IOException {
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            ArrayList<wmsBinStatusType> statusList = sql.getBinStatusTypes(applicationID);
            return statusList;
        }
       return null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("registerZone/{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String registerZone( @PathParam("applicationID") String applicationID , wmsZone zone, @Context HttpServletResponse response, @HeaderParam("userName") String userName,@HeaderParam("session") String session ) throws SQLException, IOException
    {   String result = "";
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            int cnt = sql.registerZone(zone, applicationID);
            result = Integer.toString(cnt);
            //response.setHeader("result", result);
        }
        return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("updateZone/{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String updateZone( @PathParam("applicationID") String applicationID , wmsZone zone, @Context HttpServletResponse response, @HeaderParam("userName") String userName,@HeaderParam("session") String session ) throws SQLException, IOException
    {   String result = "";
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            int cnt = sql.updateZone(zone, applicationID);
            result = Integer.toString(cnt);
        }
        return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("registerBinType/{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String registerBinType( @Context HttpServletResponse response, wmsBinType binType, @PathParam("applicationID") String applicationID, @HeaderParam("userName") String userName,@HeaderParam("session") String session ) throws SQLException, IOException
    {
        String result = "";
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            int cnt = sql.registerBinType(binType, applicationID);
            result = Integer.toString(cnt);
            response.setHeader("result", result);
        }
        return result;
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("updateBinType/{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String updateBinType(@Context HttpServletResponse response, wmsBinType binType, @PathParam("applicationID") String applicationID, @HeaderParam("userName") String userName, @HeaderParam("session") String session) throws SQLException, IOException
    {
        String result = "";
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            int cnt = sql.updateBinType(binType, applicationID);
            result = Integer.toString(cnt);
            response.setHeader("result", result);
        }
        return result;
    }

    @GET
    @Path("deleteBinType/{binTypeID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String deleteBinType(@PathParam("binTypeID") String binTypeID,  @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException
    {
        String result = "";
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            int cnt = sql.deleteBinType(binTypeID);
            result = Integer.toString(cnt);
        }
        return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("registerBinSuperType/{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String registerBinSuperType(@Context HttpServletResponse response, wmsBinSuperType binType, @PathParam("applicationID") String applicationID, @HeaderParam("userName") String userName, @HeaderParam("session") String session) throws SQLException, IOException
    {
        String result = "";
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            int cnt = sql.registerBinSuperType(binType, applicationID);
            result = Integer.toString(cnt);
            response.setHeader("result", result);
        }
        return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("updateBinSuperType/{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String updateBinSuperType(@Context HttpServletResponse response, wmsBinSuperType binType, @PathParam("applicationID") String applicationID, @HeaderParam("userName") String userName, @HeaderParam("session") String session) throws SQLException, IOException
    {
        String result = "";
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            int cnt = sql.updateBinSuperType(binType, applicationID);
            result = Integer.toString(cnt);
            response.setHeader("result", result);
        }
        return result;
    }

    @GET
    @Path("deleteBinSuperType/{binSuperTypeID}&{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String deleteBinSuperType(@PathParam("binSuperTypeID") String binSuperTypeID,@PathParam("applicationID") String applicationID,  @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException
    {
        String result = "";
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            int cnt = sql.deleteBinSuperType(binSuperTypeID, applicationID);
            result = Integer.toString(cnt);
        }
        return result;
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("registerStatus/{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String registerStatus(wmsBinStatusType statusType,@PathParam("applicationID") String applicationID , @Context HttpServletResponse response, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException
    {
        String result = "";
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            int cnt = sql.registerStatus(statusType, applicationID);
            result = Integer.toString(cnt);
            response.setHeader("result", result);
        }
        return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("updateStatus/{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public String updateStatus(wmsBinStatusType statusType,@PathParam("applicationID") String applicationID , @Context HttpServletResponse response, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException
    {
        String result = "";
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            int cnt = sql.updateStatus(statusType, applicationID);
            result = Integer.toString(cnt);
            response.setHeader("result", result);
        }
        return result;
    }



    /*  */
    /****************functions here are called from wmsReporting class */
    /* */

    @GET
    @Path("getLastReadings/{zoneID}&{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsReading> getLastReadings(@PathParam("zoneID") String zoneID,@PathParam("applicationID") String applicationID, @HeaderParam("userName") String userName,@HeaderParam("session") String session)throws SQLException, IOException {
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            ArrayList<wmsReading> dataList = wrep.getLastReadings(zoneID, applicationID);
            return dataList;
        }
        else
            return null;
    }

    @GET
    @Path("getBinActivity/{binID}&{applicationID}&{fromDate}&{toDate}")
    @Produces(MediaType.APPLICATION_JSON)
    public wmsReportTotal getBinActivity(@PathParam("binID") String binID,@PathParam("applicationID") String applicationID,@PathParam("fromDate") String fromDate,@PathParam("toDate") String toDate, @HeaderParam("userName") String userName,@HeaderParam("session") String session)throws SQLException, IOException {
        wmsReportTotal rt =null;
       /*
        rt =  new wmsReportTotal();
        rt.averageWeight="500";
        rt.totalWeight="2500";
        rt.binactivity = new ArrayList<>();
        for (int i=0;i<3;i++)
        {
            wmsBinActivity ba = new wmsBinActivity();
            ba.binID=String.valueOf(i);
            ba.clearedWeight = String.valueOf(i*100);
            rt.binactivity.add(ba);
        }           */

        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
          rt = wrep.getBinActivity(binID, applicationID, fromDate, toDate);
        }

        return rt;
    }

    @GET
    @Path("getZoneActivity/{zoneID}&{applicationID}&{fromDate}&{toDate}")
    @Produces(MediaType.APPLICATION_JSON)
    public wmsReportTotal getZoneActivity(@PathParam("zoneID") String zoneID,@PathParam("applicationID") String applicationID,@PathParam("fromDate") String fromDate,@PathParam("toDate") String toDate, @HeaderParam("userName") String userName,@HeaderParam("session") String session)throws SQLException, IOException {
        wmsReportTotal rt =null;
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            rt = wrep.getZoneActivity(zoneID, applicationID, fromDate, toDate);
        }
        return rt;
    }



    @GET
    @Path("getZoneReadings/{zoneID}&{applicationID}&{fromDate}&{toDate}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsReading> getZoneReadings(@PathParam("zoneID") String zoneID,@PathParam("applicationID")  String applicationID, @PathParam("fromDate") String fromDate,@PathParam("toDate") String toDate, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException {
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            ArrayList<wmsReading> dataList = wrep.getZoneReadings(zoneID, applicationID, fromDate, toDate);
            return dataList;
        }
        return null;

    }


    @GET
    @Path("getBinReadings/{binID}&{fromDate}&{toDate}&{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsReading> getBinReadings(@PathParam("binID") String binID, @PathParam("fromDate") String fromDate,@PathParam("toDate") String toDate, @PathParam("applicationID") String applicationID, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException {
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            //ArrayList<wmsReading> dataList = wrep.getBinReadings(binID, fromDate, toDate,applicationID);
            ArrayList<wmsReading> dataList = wrep.getViewBinReadings(binID, fromDate, toDate,applicationID);
            return dataList;
        }
        else
            return null;
    }

    @GET
    @Path("getFilteredReadingsPerMaterial/{applicationID}&{zoneID}&{fillLevel}&{material}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsReading> getFilteredReadingsPerMaterial(@PathParam("applicationID") String applicationID,@PathParam("zoneID") String zoneID, @PathParam("fillLevel")String fillLevel, @PathParam("material")String material, @HeaderParam("userName") String userName,@HeaderParam("session") String session)throws SQLException, IOException
    {
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            ArrayList<wmsReading> dataList = wrep.getFilteredReadingsPerMaterial(applicationID, zoneID, fillLevel, material);
            return dataList;
        }
        else
            return null;

    }

    @GET
    @Path("getFilledBins/{zoneID}&{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsBin> getFilledBins(@PathParam("zoneID") String zoneID, @PathParam("applicationID") String applicationID, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException
    {
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            ArrayList<wmsBin> binlist = wrep.getFilledBins(zoneID, applicationID);
            return binlist;
        }
        else
            return null;
    }

    @GET
    @Path("getFilledBinsByMaterial/{zoneID}&{applicationID}&{wasteMaterial}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public ArrayList<wmsBin> getFilledBinsByMaterial(@PathParam("zoneID") String zoneID, @PathParam("applicationID") String applicationID, @PathParam("wasteMaterial") String wasteMaterial, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException
    {
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            ArrayList<wmsBin> binlist = wrep.getFilledBinsByMaterial(zoneID, applicationID, wasteMaterial);
            return binlist;
        }
        else
            return null;

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("redirectToPage/{pageName}")
    public Response redirectToPage(@PathParam("pageName") String pageName , @Context HttpServletResponse response, @HeaderParam("userName") String userName,@HeaderParam("session") String session) throws SQLException, IOException, URISyntaxException
    {
        String key = sql.findSession(userName,session);
        if (key!=null && key.equals(session)) {
            URI location = new URI("../"+pageName+".html");
            return Response.seeOther(location).build();
        }
        else
            return Response.ok("Invalid session").build();
    }


/**--------------------------------login function */
    @GET
    @Path("loginUser/{userName}&{paswd}&{applicationID}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public sessionModel loginUser(@Context HttpServletRequest req, @PathParam("userName") String userName,@PathParam("paswd") String paswd,@PathParam("applicationID") String applicationID) throws SQLException, NoSuchAlgorithmException
    {
            sessionModel sm = sql.loginUser(userName, paswd, applicationID);
            return sm;
    }
}


































