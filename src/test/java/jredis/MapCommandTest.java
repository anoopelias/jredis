package jredis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import jredis.exception.InvalidCommand;

import org.junit.Before;
import org.junit.Test;

public class MapCommandTest {
    
    private static String[] ADD_ONE = {"Nums", "1.0", "One" };
    private static String[] ADD_TWO = {"Nums", "2.0", "Two" };
    private static String[] ADD_THREE = {"Nums", "3.0", "Three" };
    private static String[] ADD_FOUR = {"Nums", "4.0", "Four" };
    private static String[] ADD_FOUR_REVISED = {"Nums", "5.0", "Four" };
    private static String[] ADD_SIX = {"Nums", "6.0", "Six" };
    private static String[] ADD_SEVEN = {"Nums", "7.0", "Seven" };
    private static String[] ADD_EIGHT = {"Nums", "8.0", "Eight" };

    private static String[] CARD = {"Nums" };
    private static String[] CARD_NONE = {"Strings" };

    @Before
    public void setup() {
        DataMap.INSTANCE.clear();
    }

    @Test
    public void test_add_card() throws InvalidCommand {
        Command<?> command = new ZaddCommand(ADD_FOUR);
        assertEquals(Integer.valueOf(1), command.execute().value());
        
        command = new ZaddCommand(ADD_SEVEN);
        assertEquals(Integer.valueOf(1), command.execute().value());

        command = new ZcardCommand(CARD);
        assertEquals(Integer.valueOf(2), command.execute().value());
    }

    @Test
    public void test_add_repeat() throws InvalidCommand {
        Command<?> command = new ZaddCommand(ADD_FOUR);
        assertEquals(Integer.valueOf(1), command.execute().value());
        
        command = new ZaddCommand(ADD_FOUR_REVISED);
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

        String[] range = {"Nums", "2.5", "4.5"};
        Command<?> command = new ZcountCommand(range);
        assertEquals(Integer.valueOf(2), command.execute().value());
    }

    @Test
    public void test_count_inclusive() throws InvalidCommand {
        addAll();

        String[] range = {"Nums", "2.0", "4.0"};
        Command<?> command = new ZcountCommand(range);
        assertEquals(Integer.valueOf(3), command.execute().value());
    }

    @Test
    public void test_count_from_infinity_to_infinity() throws InvalidCommand {
        addAll();

        String[] range = {"Nums", "-inf", "+inf"};
        Command<?> command = new ZcountCommand(range);
        assertEquals(Integer.valueOf(7), command.execute().value());
    }

    @Test
    public void test_count_to_infinity() throws InvalidCommand {
        addAll();

        String[] range = {"Nums", "6.0", "inf"};
        Command<?> command = new ZcountCommand(range);
        assertEquals(Integer.valueOf(3), command.execute().value());
    }

    @Test
    public void test_count_infinity_to_minus_infinity() throws InvalidCommand {
        addAll();

        String[] range = {"Nums", "inf", "-inf"};
        Command<?> command = new ZcountCommand(range);
        assertEquals(Integer.valueOf(0), command.execute().value());
    }

    @Test
    public void test_count_equal_value() throws InvalidCommand {
        addAll();

        String[] range = {"Nums", "3.0", "3.0"};
        Command<?> command = new ZcountCommand(range);
        assertEquals(Integer.valueOf(1), command.execute().value());
        
        String[] range2 = {"Nums", "4.5", "4.5"};
        command = new ZcountCommand(range2);
        assertEquals(Integer.valueOf(0), command.execute().value());
    }

    @Test
    public void test_range() throws InvalidCommand {
        addAll();
        
        String[] args = {"Nums", "3", "5"};
        ZrangeCommand command = new ZrangeCommand(args);
        Iterator<Element> results = command.execute().value().iterator();
        
        Element element = results.next();
        assertEquals("Four", element.getMember());
        element = results.next();
        assertEquals("Six", element.getMember());
        element = results.next();
        assertEquals("Seven", element.getMember());
        assertFalse(results.hasNext());

    }

    @Test
    public void test_range_negative_start() throws InvalidCommand {
        addAll();
        
        String[] args = {"Nums", "-3", "5"};
        ZrangeCommand command = new ZrangeCommand(args);
        Iterator<Element> results = command.execute().value().iterator();
        
        Element element = results.next();
        assertEquals("Six", element.getMember());
        element = results.next();
        assertEquals("Seven", element.getMember());
        assertFalse(results.hasNext());

    }

    @Test
    public void test_range_negative_end() throws InvalidCommand {
        addAll();
        
        String[] args = {"Nums", "1", "-5"};
        ZrangeCommand command = new ZrangeCommand(args);
        Iterator<Element> results = command.execute().value().iterator();
        
        Element element = results.next();
        assertEquals("Two", element.getMember());
        element = results.next();
        assertEquals("Three", element.getMember());
        assertFalse(results.hasNext());

    }

    @Test
    public void test_range_same_positive() throws InvalidCommand {
        addAll();
        
        String[] args = {"Nums", "3", "3"};
        ZrangeCommand command = new ZrangeCommand(args);
        Iterator<Element> results = command.execute().value().iterator();
        
        Element element = results.next();
        assertEquals("Four", element.getMember());
        assertFalse(results.hasNext());

    }

    @Test
    public void test_range_same_negative() throws InvalidCommand {
        addAll();
        
        String[] args = {"Nums", "-2", "-2"};
        ZrangeCommand command = new ZrangeCommand(args);
        Iterator<Element> results = command.execute().value().iterator();
        
        Element element = results.next();
        assertEquals("Seven", element.getMember());
        assertFalse(results.hasNext());

    }

    
    @Test
    public void test_range_with_scores() throws InvalidCommand {
        addAll();
        
        String[] args = {"Nums", "1", "4", "WITHSCORES"};
        ZrangeCommand command = new ZrangeCommand(args);
        assertTrue(command.execute().value().isScored());

    }

    
    private void addAll() throws InvalidCommand {
        new ZaddCommand(ADD_SIX).execute();
        new ZaddCommand(ADD_SEVEN).execute();
        new ZaddCommand(ADD_ONE).execute();
        new ZaddCommand(ADD_THREE).execute();
        new ZaddCommand(ADD_TWO).execute();
        new ZaddCommand(ADD_FOUR).execute();
        new ZaddCommand(ADD_EIGHT).execute();
    }

}
