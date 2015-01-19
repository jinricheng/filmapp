//package cat.udl.eps.softarch;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.xquery.*;

/**
 * Created by http://rhizomik.net/~roberto/
 */
public class XQueryHelper {
    private static final Logger  log = Logger.getLogger(XQueryHelper.class.getName());

    private XQPreparedExpression expr;
    private XQConnection         conn;

    private JAXBContext          jaxbContext;
    private Unmarshaller         jaxbUnmarshaller;

    static final String apiURL   = "http://www.omdbapi.com/?t=Jin&amp;y=&amp;plot=short&amp;r=xml";
    static final String albumsXQ =
            "declare namespace mmd=\"http://musicbrainz.org/ns/mmd-2.0#\";\n"
                    + "declare variable $doc external;\n"
                    + "for $r in $doc//mmd:release\n"
                    + "let $years-from-date:=$r/mmd:date[matches(text(),\"^\\d{4}-\\d{2}-\\d{2}$\")]/year-from-date(text())\n"
                    + "let $years:=$r/mmd:date[matches(text(),\"^\\d{4}$\")]\n"
                    + "return\n"
                    + "<song>\n"
                    + "  <title>{$r/mmd:title/text()}</title>\n"
                    + "  <artist>{$doc//mmd:artist/mmd:name/text()}</artist>\n"
                    + "  <countries>{distinct-values($r//mmd:country)}</countries>\n"
                    + "  <year>{min(($years,$years-from-date))}</year>\n"
                    + "</song>";

    static final String xquery =  "declare variable $doc := doc(\"http://www.omdbapi.com/?t=History&amp;y=&amp;plot=short&amp;r=xml\");"+
    "for $c in $doc/root/movie\n"+
    "return \n"
            +"<song>\n"
            +"  <title>{data($c/@title)}</title>\n"
            +"  <year>{data($c/@year)}</year>\n"
            +"  <mpaa_rating>{data($c/@rated)}</mpaa_rating>\n"
            +"  <runtime>{data($c/@runtime)}</runtime>\n"
            +"  <synopsis>{data($c/@plot)}</synopsis>\n"
            +"  <poster>{data($c/@poster)}</poster>\n"
            +"  <email>omdb@filmapp.com</email>\n"
            +"</song>";
    @XmlRootElement
    private static class Song {
        @XmlElement String title;
        @XmlElement String year;
        @XmlElement String synopsis;
        @XmlElement String poster;

        @Override
        public String toString() {
            return "Title: "+title+"\n"+"Year: "+year+"\n"+"Synopsis: "+synopsis+"\n"+"poster: "+poster+"\n";
        }
    }

    XQueryHelper(String xquery, URL url)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, XQException, IOException, JAXBException {
        URLConnection urlconn = url.openConnection();
        urlconn.setReadTimeout(50000);

        XQDataSource xqds = (XQDataSource) Class.forName("net.sf.saxon.xqj.SaxonXQDataSource").newInstance();
        this.conn = xqds.getConnection();
        this.expr = conn.prepareExpression(xquery);  System.out.println("hdjshdsjd");
       // this.expr.bindDocument(new javax.xml.namespace.QName("doc"), urlconn.getInputStream(), null, null);

        this.jaxbContext = JAXBContext.newInstance(Song.class);
        this.jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    }

    ArrayList<Song> getSongs() {
        ArrayList<Song> songs = new ArrayList<Song>();
        try {
            XQResultSequence rs = this.expr.executeQuery();
            while (rs.next()) {
                XQItem item = rs.getItem();
                Song song = (Song) jaxbUnmarshaller.unmarshal(item.getNode());
                songs.add(song);
            }
        }
        catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        finally { close(); }
        return songs;
    }

    private void close() {
        try {
            this.expr.close();
            this.conn.close();
        } catch (XQException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            XQueryHelper xQueryHelper = new XQueryHelper(xquery, new URL(apiURL));
            ArrayList<Song> songs = xQueryHelper.getSongs();
            for (Song song : songs)
                System.out.println(song);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}