package org.example.models;

import java.util.Date;

public class Coin {
    private String partNumber;
    private Date dt;
    private String cname;
    private String sname;
    private String nominal;
    private String metal;
    private String avers;
    private byte[] aversBig;
    private String revers;
    private byte[] reversBig;
    private String circulation;
    private int availability;

    public String getCirculation() {
        return circulation;
    }

    public void setCirculation(String circulation) {
        this.circulation = circulation;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getMetal() {
        return metal;
    }

    public void setMetal(String metal) {
        this.metal = metal;
    }

    public String getAvers() {
        return avers;
    }

    public void setAvers(String avers) {
        this.avers = avers;
    }

    public byte[] getAversBig() {
        return aversBig;
    }

    public void setAversBig(byte[] aversBig) {
        this.aversBig = aversBig;
    }

    public String getRevers() {
        return revers;
    }

    public void setRevers(String revers) {
        this.revers = revers;
    }

    public byte[] getReversBig() {
        return reversBig;
    }

    public void setReversBig(byte[] reversBig) {
        this.reversBig = reversBig;
    }

    public Coin(String partNumber, Date dt, String cname, String sname, String nominal, String metal, String avers,
                byte[] aversBig, String revers, byte[] reversBig, String circulation, int avialability) {
        this.partNumber = partNumber;
        this.dt = dt;
        this.cname = cname;
        this.sname = sname;
        this.nominal = nominal;
        this.metal = metal;
        this.avers = avers;
        this.aversBig = aversBig;
        this.revers = revers;
        this.reversBig = reversBig;
        this.circulation = circulation;
        this.availability = avialability;
    }

    public Coin() {
    }

//    public File toFile(byte[] bytes) throws IOException {
//        Path target = Paths.get("c:\\temp\\new.jpg");
//
//        // convert byte[] back to a BufferedImage
//        InputStream is = new ByteArrayInputStream(bytes);
//        BufferedImage newBi = ImageIO.read(is);
//
//        // save it
//        ImageIO.write(newBi, "jpg", target.toFile());
//        return new File("c:\\temp\\new.jpg");
//
//    }

    @Override
    public String toString() {
        return "Coin{" +
                "partNumber='" + partNumber + '\'' +
                ", dt=" + dt +
                ", cname='" + cname + '\'' +
                ", sname='" + sname + '\'' +
                ", nominal='" + nominal + '\'' +
                ", metal='" + metal + '\'' +
                ", avers='" + avers + '\'' +
                ", aversBig='" + aversBig + '\'' +
                ", revers='" + revers + '\'' +
                ", reversBig='" + reversBig + '\'' +
                ", circulation=" + circulation +
                ", availability=" + availability +
                '}';
    }
}
