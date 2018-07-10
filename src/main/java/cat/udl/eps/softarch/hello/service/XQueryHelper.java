package cat.udl.eps.softarch.hello.service;

import cat.udl.eps.softarch.hello.model.Film;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.xquery.*;

/**
 * Created by http://rhizomik.net/~roberto/
 */
public class XQueryHelper {
    private static final Logger log = Logger.getLogger(XQueryHelper.class.getName());

    private XQPreparedExpression expr;
    private XQConnection conn;

    private JAXBContext jaxbContext;
    private Unmarshaller jaxbUnmarshaller;

    final String apiURL = "http://www.omdbapi.com/";

    private String xquery;

    private void setQuery(String title) {
        xquery = "declare variable $doc := doc(\"http://www.omdbapi.com/?apikey=90f720d8&amp;t=" + title + "&amp;r=xml\");" +
                "for $c in $doc/root/movie\n" +
                "return \n"
                + "<film>\n"
                + "  <title>{data($c/@title)}</title>\n"
                + "  <year>{data($c/@year)}</year>\n"
                + "  <mpaa_rating>{data($c/@rated)}</mpaa_rating>\n"
                + "  <runtime>{data($c/@runtime)}</runtime>\n"
                + "  <synopsis>{data($c/@plot)}</synopsis>\n"
                + "  <poster>{data($c/@poster)}</poster>\n"
                + "  <email>omdb@filmapp.com</email>\n"
                + "</film>";
    }

    public List<Film> getListFilm(String title) throws ClassNotFoundException, InstantiationException, IllegalAccessException, XQException, IOException, JAXBException {
        title = title.replaceAll(" ", "+");
        this.setQuery(title);
        URLConnection urlconn = new URL(apiURL).openConnection();
        urlconn.setReadTimeout(50000);

        XQDataSource xqds = (XQDataSource) Class.forName("net.sf.saxon.xqj.SaxonXQDataSource").newInstance();
        this.conn = xqds.getConnection();
        this.expr = conn.prepareExpression(xquery);
        this.jaxbContext = JAXBContext.newInstance(Film.class);
        this.jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        System.out.println(xquery);
        List<Film> result = this.getFilms();

        return result;
    }

    private ArrayList<Film> getFilms() {
        ArrayList<Film> films = new ArrayList<Film>();
        try {
            XQResultSequence rs = this.expr.executeQuery();
            while (rs.next()) {
                XQItem item = rs.getItem();
                Film film = (Film) jaxbUnmarshaller.unmarshal(item.getNode());
                films.add(film);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
        } finally {
            close();
        }
        return films;
    }

    private void close() {
        try {
            this.expr.close();
            this.conn.close();
        } catch (XQException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }
}


    /*@XmlRootElement
    private static class Film {
        @XmlElement String title;
        @XmlElement String year;
        @XmlElement String synopsis;
        @XmlElement String mpaa_rating;
        @XmlElement String poster;

        @Override
        public String toString() {
            return "Title: "+title+"\n"+"Year: "+year+"\n"+"mpaa_rating: "+mpaa_rating+"\n"+"Synopsis: "+synopsis+"\n"+"poster: "+poster+"\n"+"Email:omdb@filmapp.com"+"\n";
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

        this.jaxbContext = JAXBContext.newInstance(Film.class);
        this.jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    }

    ArrayList<Film> getSongs() {
        ArrayList<Film> songs = new ArrayList<Film>();
        try {
            XQResultSequence rs = this.expr.executeQuery();
            while (rs.next()) {
                XQItem item = rs.getItem();
                Film song = (Film) jaxbUnmarshaller.unmarshal(item.getNode());
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
            String title ="Toy Story";
            title = title.replaceAll(" ","+");
            XQueryHelper.setQuery(title);


            XQueryHelper xQueryHelper = new XQueryHelper(xquery, new URL(apiURL));
            ArrayList<Film> films = xQueryHelper.getSongs();
            for (Film film :films)
                System.out.println(film);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    }*/