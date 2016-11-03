package ninja.zilles.landmarks;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by zilles on 11/1/16.
 */
public class LandmarkTest {
    Landmark m_landmark;

    @Before
    public void setUp() throws Exception {
//        System.out.println(Landmark.example);
        Gson gson = new Gson();
        m_landmark = (Landmark) gson.fromJson(Landmark.example, Landmark.class);

    }

    @Test
    public void getType_of_landmark() throws Exception {
        assertEquals(m_landmark.getTypeOfLandmark(), "Building");
    }

    @Test
    public void getAddress() throws Exception {
        assertEquals(m_landmark.getAddress(), "346-352 N Neil St");
    }

    @Test
    public void getNotes() throws Exception {
        assertEquals(m_landmark.getNotes(), "also on National Register Historic Places");
    }

    @Test
    public void getOwnership() throws Exception {
        assertEquals(m_landmark.getOwnership(), "Museum Group");
    }

    @Test
    public void getDate_designated() throws Exception {
        assertEquals(m_landmark.getDateDesignated(), "1998-03-17T00:00:00");
    }

    @Test
    public void getLandmark_name() throws Exception {
        assertEquals(m_landmark.getLandmarkName(), "Orpheum Theater");
    }

    @Test
    public void getLocation() throws Exception {
        Landmark.Location location = m_landmark.getLocation();
        assertEquals(location.getHumanAddress(), "{\"address\":\"346-352 N Neil St\",\"city\":\"\",\"state\":\"\",\"zip\":\"\"}");
        assertEquals(location.getLatitude(), 40.1194838838, .1);
        assertEquals(location.getLongitude(), -88.2425214665, .1);
        assertFalse(location.needsRecording());
    }

}