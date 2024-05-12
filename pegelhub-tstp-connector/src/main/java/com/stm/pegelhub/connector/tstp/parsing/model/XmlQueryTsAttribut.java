package com.stm.pegelhub.connector.tstp.parsing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlQueryTsAttribut {
    @XmlElement(name = "ZRID")
    private String zrid;
    @XmlElement(name = "MAXFOCUS-Start")
    private String maxFocusStart;
    @XmlElement(name = "MAXFOCUS-End")
    private String maxFocusEnd;
    @XmlElement(name = "MAXQUAL")
    private String maxQual;
    @XmlElement(name = "PARAMETER")
    private String parameter;
    @XmlElement(name = "ORT")
    private String ort;
    @XmlElement(name = "DEFART")
    private String defArt;
    @XmlElement(name = "AUSSAGE")
    private String aussage;
    @XmlElement(name = "XDISTANZ")
    private String xDistanz;
    @XmlElement(name = "XFAKTOR")
    private String xFaktor;
    @XmlElement(name = "HERKUNFT")
    private String herkunft;
    @XmlElement(name = "REIHENART")
    private String reihenArt;
    @XmlElement(name = "VERSION")
    private String version;
    @XmlElement(name = "X")
    private String x;
    @XmlElement(name = "Y")
    private String y;
    @XmlElement(name = "GUELTVON")
    private String gueltVon;
    @XmlElement(name = "GUELTBIS")
    private String gueltBis;
    @XmlElement(name = "EINHEIT")
    private String einheit;
    @XmlElement(name = "MESSGENAU")
    private String messGenau;
    @XmlElement(name = "FTOLERANZ")
    private String fToleranz;
    @XmlElement(name = "FTOLREL")
    private String fTolRel;
    @XmlElement(name = "NWGRENZE")
    private String nwGrenze;
    @XmlElement(name = "SUBORT")
    private String subOrt;
    @XmlElement(name = "KOMMENTAR")
    private String kommentar;
    @XmlElement(name = "HOEHE")
    private String hoehe;
    @XmlElement(name = "YTYP")
    private String yTyp;
    @XmlElement(name = "XEINHEIT")
    private String xEinheit;
    @XmlElement(name = "QUELLE")
    private String quelle;
    @XmlElement(name = "PUBLIZIERT")
    private String publiziert;
    @XmlElement(name = "PARMERKMAL")
    private String parMerkmal;
    @XmlElement(name = "HAUPTREIHE")
    private String hauptReihe;
    @XmlElement(name = "MAXTEXTFOCUS-Start")
    private String maxTextFocusStart;
    @XmlElement(name = "MAXTEXTFOCUS-End")
    private String maxTextFocusEnd;

    public XmlQueryTsAttribut() {
        this.zrid = "PK8n4XrPPUfYpndH6GLH6A";
        this.maxFocusStart = "1960-05-06T12:02:00Z";
        this.maxFocusEnd = "2001-08-01T00:00:00Z";
        this.maxQual = "1";
        this.parameter = "Wasserstand";
        this.ort = "24004501";
        this.defArt = "K";
        this.aussage = "";
        this.xDistanz = "E";
        this.xFaktor = "1";
        this.herkunft = "O";
        this.reihenArt = "Z";
        this.version = "0";
        this.x = "2526320";
        this.y = "5640320";
        this.gueltVon = "";
        this.gueltBis = "";
        this.einheit = "cm";
        this.messGenau = "0.0000";
        this.fToleranz = "1.0000";
        this.fTolRel = "False";
        this.nwGrenze = "0.0000";
        this.subOrt = "";
        this.kommentar = "";
        this.hoehe = "83";
        this.yTyp = "";
        this.xEinheit = "";
        this.quelle = "";
        this.publiziert = "F";
        this.parMerkmal = "";
        this.hauptReihe = "T";
        this.maxTextFocusStart = "1990-05-29T11:33:00Z";
        this.maxTextFocusEnd = "1995-06";
    }


    public String getZrid() {
        return zrid;
    }

    public void setZrid(String zrid) {
        this.zrid = zrid;
    }

    public String getMaxFocusStart() {
        return maxFocusStart;
    }

    public void setMaxFocusStart(String maxFocusStart) {
        this.maxFocusStart = maxFocusStart;
    }

    public String getMaxFocusEnd() {
        return maxFocusEnd;
    }

    public void setMaxFocusEnd(String maxFocusEnd) {
        this.maxFocusEnd = maxFocusEnd;
    }

    public String getMaxQual() {
        return maxQual;
    }

    public void setMaxQual(String maxQual) {
        this.maxQual = maxQual;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getDefArt() {
        return defArt;
    }

    public void setDefArt(String defArt) {
        this.defArt = defArt;
    }

    public String getAussage() {
        return aussage;
    }

    public void setAussage(String aussage) {
        this.aussage = aussage;
    }

    public String getxDistanz() {
        return xDistanz;
    }

    public void setxDistanz(String xDistanz) {
        this.xDistanz = xDistanz;
    }

    public String getxFaktor() {
        return xFaktor;
    }

    public void setxFaktor(String xFaktor) {
        this.xFaktor = xFaktor;
    }

    public String getHerkunft() {
        return herkunft;
    }

    public void setHerkunft(String herkunft) {
        this.herkunft = herkunft;
    }

    public String getReihenArt() {
        return reihenArt;
    }

    public void setReihenArt(String reihenArt) {
        this.reihenArt = reihenArt;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getGueltVon() {
        return gueltVon;
    }

    public void setGueltVon(String gueltVon) {
        this.gueltVon = gueltVon;
    }

    public String getGueltBis() {
        return gueltBis;
    }

    public void setGueltBis(String gueltBis) {
        this.gueltBis = gueltBis;
    }

    public String getEinheit() {
        return einheit;
    }

    public void setEinheit(String einheit) {
        this.einheit = einheit;
    }

    public String getMessGenau() {
        return messGenau;
    }

    public void setMessGenau(String messGenau) {
        this.messGenau = messGenau;
    }

    public String getfToleranz() {
        return fToleranz;
    }

    public void setfToleranz(String fToleranz) {
        this.fToleranz = fToleranz;
    }

    public String getfTolRel() {
        return fTolRel;
    }

    public void setfTolRel(String fTolRel) {
        this.fTolRel = fTolRel;
    }

    public String getNwGrenze() {
        return nwGrenze;
    }

    public void setNwGrenze(String nwGrenze) {
        this.nwGrenze = nwGrenze;
    }

    public String getSubOrt() {
        return subOrt;
    }

    public void setSubOrt(String subOrt) {
        this.subOrt = subOrt;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public String getHoehe() {
        return hoehe;
    }

    public void setHoehe(String hoehe) {
        this.hoehe = hoehe;
    }

    public String getyTyp() {
        return yTyp;
    }

    public void setyTyp(String yTyp) {
        this.yTyp = yTyp;
    }

    public String getxEinheit() {
        return xEinheit;
    }

    public void setxEinheit(String xEinheit) {
        this.xEinheit = xEinheit;
    }

    public String getQuelle() {
        return quelle;
    }

    public void setQuelle(String quelle) {
        this.quelle = quelle;
    }

    public String getPubliziert() {
        return publiziert;
    }

    public void setPubliziert(String publiziert) {
        this.publiziert = publiziert;
    }

    public String getParMerkmal() {
        return parMerkmal;
    }

    public void setParMerkmal(String parMerkmal) {
        this.parMerkmal = parMerkmal;
    }

    public String getHauptReihe() {
        return hauptReihe;
    }

    public void setHauptReihe(String hauptReihe) {
        this.hauptReihe = hauptReihe;
    }

    public String getMaxTextFocusStart() {
        return maxTextFocusStart;
    }

    public void setMaxTextFocusStart(String maxTextFocusStart) {
        this.maxTextFocusStart = maxTextFocusStart;
    }

    public String getMaxTextFocusEnd() {
        return maxTextFocusEnd;
    }

    public void setMaxTextFocusEnd(String maxTextFocusEnd) {
        this.maxTextFocusEnd = maxTextFocusEnd;
    }
}
