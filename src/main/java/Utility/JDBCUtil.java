package Utility;

import Service.ServiceInstance;

import java.sql.*;

public class JDBCUtil {
    private static Connection getConn() {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/samp_db";
        String username = "root";
        String password = "910529";
        Connection conn = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static int insert(ServiceInstance service) {
        Connection conn = getConn();
        int i = 0;
        String sql = "insert into service (name,uid,address,registrationTimeUTC,startPath,checkPath,stopPath,configPath,configType) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1, service.getName());
            pstmt.setString(2, service.getId());
            pstmt.setString(3, service.getAddress());
            pstmt.setString(4, String.valueOf(service.getRegistrationTimeUTC()));
            pstmt.setString(5, service.getStartPath());
            pstmt.setString(6, service.getCheckPath());
            pstmt.setString(7, service.getStopPath());
            pstmt.setString(8, service.getConfigPath());
            pstmt.setString(9, service.getConfigType().getTypeStr());
            i = pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }


    public static int update(ServiceInstance service) {
        Connection conn = getConn();
        int i = 0;
        String sql = "update  service set name = ?,address = ?,registrationTimeUTC = ?,startPath = ?,checkPath =?,stopPath = ?,configPath=?,configType =? " + " where uid = ?";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1, service.getName());
            pstmt.setString(2, service.getAddress());
            pstmt.setString(3, String.valueOf(service.getRegistrationTimeUTC()));
            pstmt.setString(4, service.getStartPath());
            pstmt.setString(5, service.getCheckPath());
            pstmt.setString(6, service.getStopPath());
            pstmt.setString(7, service.getConfigPath());
            pstmt.setString(8, service.getConfigType().getTypeStr());
            pstmt.setString(9, service.getId());
            i = pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }


    public static int delete(String uuid) {
        Connection conn = getConn();
        int i = 0;
        String sql = "delete  service  where uid = ?";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1, uuid);
            i = pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static int updateConfig(Blob blob , String uuid){
        Connection conn = getConn();
        int i = 0;
        String sql = "update service set configFile = ? " + " where uid = ?";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setBlob(1, blob);
            pstmt.setString(2, uuid);
            i = pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }


    public static int updateStatus(String status , String uuid){
        Connection conn = getConn();
        int i = 0;
        String sql = "update service set status = ? " + " where uid = ?";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setString(2, uuid);
            i = pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

}
