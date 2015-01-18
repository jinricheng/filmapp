package cat.udl.eps.softarch.hello.service;


import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.json.XML;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;
import sun.net.www.protocol.http.HttpURLConnection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.xquery.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by http://rhizomik.net/~roberto/
 */
public class XQueryHelper {
    private static final Logger  log = Logger.getLogger(XQueryHelper.class.getName());

    private XQPreparedExpression expr;
    private XQConnection         conn;

    private JAXBContext          jaxbContext;
    private Unmarshaller         jaxbUnmarshaller;

    static final String apiURL   = "http://musicbrainz.org/ws/2/artist/cc2c9c3c-b7bc-4b8b-84d8-4fbd8779e493?inc=releases";
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

    @XmlRootElement
    private static class Song {
        @XmlElement String title;
        @XmlElement String artist;
        @XmlElement String countries;
        @XmlElement Integer year;

        @Override
        public String toString() {
            return "Title: "+title+"\n"+"Artist: "+artist+"\n"+"Countries: "+countries+"\n"+"Year: "+year+"\n";
        }
    }

    XQueryHelper(String xquery, URL url)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, XQException, IOException, JAXBException {
        URLConnection urlconn = url.openConnection();
        urlconn.setReadTimeout(50000);

        XQDataSource xqds = (XQDataSource) Class.forName("net.sf.saxon.xqj.SaxonXQDataSource").newInstance();
        this.conn = xqds.getConnection();
        this.expr = conn.prepareExpression(xquery);
        this.expr.bindDocument(new javax.xml.namespace.QName("doc"), urlconn.getInputStream(), null, null);

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
/*
    public static void main(String[] args) throws IOException {
        /*try {
            XQueryHelper xQueryHelper = new XQueryHelper(albumsXQ, new URL(apiURL));
            ArrayList<Song> songs = xQueryHelper.getSongs();
            for (Song song : songs)
                System.out.println(song);
        } catch (Exception e){
            e.printStackTrace();
        }
        String sURL = "http://api.rottentomatoes.com/api/public/v1.0/movies/770672122.json?apikey=2dkxg44jru5rt5rsqezxbnek";
        String URL = "http://filmsapp.herokuapp.com/films.json";
        // Connect to the URL using java's native library
        URL url = new URL(sURL);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        // Convert to a JSON object to print data
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //convert the input stream to a json element
        JsonObject rootobj = root.getAsJsonObject(); //may be an array, may be an object.
        System.out.println(rootobj);
        //zipcode=rootobj.get("zipcode").getAsString();//just grab the zipcode

        String str = rootobj.toString();
        str = str.substring(1,str.length()-1);
        str = "{"+str+"}";
        System.out.println(str);
        JSON json = JSONSerializer.toJSON(str);
        XMLSerializer xmlSerializer = new XMLSerializer();
        String xml = xmlSerializer.write( json );

        System.out.println(xml);
    }
*/
}
