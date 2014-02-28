package jredis;

import static org.junit.Assert.assertEquals;
import jredis.exception.InvalidCommand;

import org.junit.Before;
import org.junit.Test;

public class MapCommandTest {
    
    private static String[] ADD_SG4 = {"Phones", "45.62", "SamsungGalaxyS4" };
    private static String[] ADD_SG4_REVISED = {"Phones", "49.29", "SamsungGalaxyS4" };
    private static String[] ADD_IP5S = {"Phones", "63.72", "iPhone5s" };
    private static String[] ADD_IP5C = {"Phones", "54.32", "iPhone5c" };
    private static String[] ADD_SG5 = {"Phones", "67.17", "SamsungGalaxyS5" };
    private static String[] ADD_SG3 = {"Phones", "25.03", "SamsungGalaxyS3" };
    private static String[] ADD_MOX = {"Phones", "36.85", "MotoX" };
    private static String[] ADD_MOG = {"Phones", "14.95", "MotoG" };

    private static String[] CARD = {"Phones" };
    private static String[] CARD_NONE = {"Shirts" };
    
    @Before
    public void setup() {
        DataMap.INSTANCE.clear();
    }

    @Test
    public void test_add_card() throws InvalidCommand {
        Command<?> command = new ZaddCommand(ADD_SG4);
        assertEquals(Integer.valueOf(1), command.execute().value());
        
        command = new ZaddCommand(ADD_IP5S);
        assertEquals(Integer.valueOf(1), command.execute().value());

        command = new ZcardCommand(CARD);
        assertEquals(Integer.valueOf(2), command.execute().value());
    }

    @Test
    public void test_add_repeat() throws InvalidCommand {
        Command<?> command = new ZaddCommand(ADD_SG4);
        assertEquals(Integer.valueOf(1), command.execute().value());
        
        command = new ZaddCommand(ADD_SG4_REVISED);
        assertEquals(Integer.valueOf(0), command.execute().value());

        command = new ZcardCommand(CARD);
        assertEquals(Integer.valueOf(1), command.execute().value());
    }

    @Test
    public void test_card_zero() throws InvalidCommand {
        Command<?> command = new ZcardCommand(CARD_NONE);
        assertEquals(Integer.valueOf(0), command.execute().value());
        
    }

    @Test
    public void test_count() throws InvalidCommand {
        addAll();

        String[] range = {"Phones", "40", "60"};
        Command<?> command = new ZcountCommand(range);
        assertEquals(Integer.valueOf(2), command.execute().value());
    }

    @Test
    public void test_count_inclusive() throws InvalidCommand {
        addAll();

        String[] range = {"Phones", "36.85", "54.32"};
        Command<?> command = new ZcountCommand(range);
        assertEquals(Integer.valueOf(3), command.execute().value());
    }

    @Test
    public void test_count_from_infinity_to_infinity() throws InvalidCommand {
        addAll();

        String[] range = {"Phones", "-inf", "+inf"};
        Command<?> command = new ZcountCommand(range);
        assertEquals(Integer.valueOf(7), command.execute().value());
    }

    @Test
    public void test_count_to_infinity() throws InvalidCommand {
        addAll();

        String[] range = {"Phones", "50", "inf"};
        Command<?> command = new ZcountCommand(range);
        assertEquals(Integer.valueOf(3), command.execute().value());
    }

    private void addAll() throws InvalidCommand {
        Command<?> command = new ZaddCommand(ADD_IP5C);
        command.execute();
        command = new ZaddCommand(ADD_IP5S);
        command.execute();
        command = new ZaddCommand(ADD_MOG);
        command.execute();
        command = new ZaddCommand(ADD_MOX);
        command.execute();
        command = new ZaddCommand(ADD_SG3);
        command.execute();
        command = new ZaddCommand(ADD_SG4);
        command.execute();
        command = new ZaddCommand(ADD_SG5);
        command.execute();
    }

}
