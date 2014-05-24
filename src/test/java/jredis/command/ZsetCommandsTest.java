package jredis.command;

import static jredis.TestUtil.h;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import jredis.DB;
import jredis.domain.BinaryString;
import jredis.domain.Element;
import jredis.exception.InvalidCommand;

import org.junit.Before;
import org.junit.Test;

public class ZsetCommandsTest {
    
    private static BinaryString[] ADD_ONE = h(new String[]{"Nums", "1.0", "One" });
    private static BinaryString[] ADD_TWO = h(new String[]{"Nums", "2.0", "Two" });
    private static BinaryString[] ADD_THREE = h(new String[]{"Nums", "3.0", "Three" });
    private static BinaryString[] ADD_FOUR = h(new String[]{"Nums", "4.0", "Four" });
    private static BinaryString[] ADD_FOUR_REVISED = h(new String[]{"Nums", "5.0", "Four" });
    private static BinaryString[] ADD_SIX = h(new String[]{"Nums", "6.0", "Six" });
    private static BinaryString[] ADD_SEVEN = h(new String[]{"Nums", "7.0", "Seven" });
    private static BinaryString[] ADD_EIGHT = h(new String[]{"Nums", "8.0", "Eight" });
    private static BinaryString[] ADD_INFINITY = h(new String[]{"Nums", "inf", "Infinity" });
    private static BinaryString[] ADD_NEGATIVE_INFINITY = h(new String[]{"Nums", "-inf", "NegativeInfinity" });
    private static BinaryString[] ADD_POSITIVE_INFINITY = h(new String[]{"Nums", "+inf", "PositiveInfinity" });

    private static BinaryString[] ADD_INTEGER = h(new String[]{"Integer", "12", "Tweleve" });

    private static BinaryString[] CARD = h(new String[]{"Nums" });
    private static BinaryString[] CARD_NONE = h(new String[]{"Strings" });

    @Before
    public void setup() {
        DB.INSTANCE.clear();
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
    public void test_add_infinity() throws InvalidCommand {
        Command<?> command = new ZaddCommand(ADD_INFINITY);
        assertEquals(Integer.valueOf(1), command.execute().value());
        
        BinaryString[] range = h(new String[]{"Nums", "5", "+inf"});
        command = new ZcountCommand(range);
        assertEquals(Integer.valueOf(1), command.execute().value());
        
        BinaryString[] args = h(new String[]{"Nums", "0", "2"});
        ZrangeCommand rangeCommand = new ZrangeCommand(args);
        Iterator<Element> results = rangeCommand.execute().value().getElements().iterator();
        
        Element element = results.next();
        assertEquals("Infinity", element.getMember());

    }
    
    @Test
    public void test_add_positive_infinity() throws InvalidCommand {
        Command<?> command = new ZaddCommand(ADD_POSITIVE_INFINITY);
        assertEquals(Integer.valueOf(1), command.execute().value());
        
        BinaryString[] range = h(new String[]{"Nums", "5", "inf"});
        command = new ZcountCommand(range);
        assertEquals(Integer.valueOf(1), command.execute().value());
        
        BinaryString[] args = h(new String[]{"Nums", "0", "2"});
        ZrangeCommand rangeCommand = new ZrangeCommand(args);
        Iterator<Element> results = rangeCommand.execute().value().getElements().iterator();
        
        Element element = results.next();
        assertEquals("PositiveInfinity", element.getMember());

    }

    @Test
    public void test_add_negative_infinity() throws InvalidCommand {
        Command<?> command = new ZaddCommand(ADD_NEGATIVE_INFINITY);
        assertEquals(Integer.valueOf(1), command.execute().value());
        
        BinaryString[] range = h(new String[]{"Nums", "-inf", "inf"});
        command = new ZcountCommand(range);
        assertEquals(Integer.valueOf(1), command.execute().value());
        
        BinaryString[] args = h(new String[]{"Nums", "0", "2"});
        ZrangeCommand rangeCommand = new ZrangeCommand(args);
        Iterator<Element> results = rangeCommand.execute().value().getElements().iterator();
        
        Element element = results.next();
        assertEquals("NegativeInfinity", element.getMember());

    }


    @Test
    public void test_count() throws InvalidCommand {
        addAll();

        BinaryString[] range = h(new String[]{"Nums", "2.5", "4.5"});
        Command<?> command = new ZcountCommand(range);
        assertEquals(Integer.valueOf(2), command.execute().value());
    }

    @Test
    public void test_count_inclusive() throws InvalidCommand {
        addAll();

        BinaryString[] range = h(new String[]{"Nums", "2.0", "4.0"});
        Command<?> command = new ZcountCommand(range);
        assertEquals(Integer.valueOf(3), command.execute().value());
    }

    @Test
    public void test_count_from_infinity_to_infinity() throws InvalidCommand {
        addAll();

        BinaryString[] range = h(new String[]{"Nums", "-inf", "+inf"});
        Command<?> command = new ZcountCommand(range);
        assertEquals(Integer.valueOf(7), command.execute().value());
    }

    @Test
    public void test_count_to_infinity() throws InvalidCommand {
        addAll();

        BinaryString[] range = h(new String[]{"Nums", "6.0", "inf"});
        Command<?> command = new ZcountCommand(range);
        assertEquals(Integer.valueOf(3), command.execute().value());
    }

    @Test
    public void test_count_infinity_to_minus_infinity() throws InvalidCommand {
        addAll();

        BinaryString[] range = h(new String[]{"Nums", "inf", "-inf"});
        Command<?> command = new ZcountCommand(range);
        assertEquals(Integer.valueOf(0), command.execute().value());
    }

    @Test
    public void test_count_equal_value() throws InvalidCommand {
        addAll();

        BinaryString[] range = h(new String[]{"Nums", "3.0", "3.0"});
        Command<?> command = new ZcountCommand(range);
        assertEquals(Integer.valueOf(1), command.execute().value());
        
        BinaryString[] range2 = h(new String[]{"Nums", "4.5", "4.5"});
        command = new ZcountCommand(range2);
        assertEquals(Integer.valueOf(0), command.execute().value());
    }

    @Test
    public void test_range() throws InvalidCommand {
        addAll();
        
        BinaryString[] args = h(new String[]{"Nums", "3", "5"});
        ZrangeCommand command = new ZrangeCommand(args);
        Iterator<Element> results = command.execute().value().getElements().iterator();
        
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
        
        BinaryString[] args = h(new String[]{"Nums", "-3", "5"});
        ZrangeCommand command = new ZrangeCommand(args);
        Iterator<Element> results = command.execute().value().getElements().iterator();
        
        Element element = results.next();
        assertEquals("Six", element.getMember());
        element = results.next();
        assertEquals("Seven", element.getMember());
        assertFalse(results.hasNext());

    }

    @Test
    public void test_range_negative_end() throws InvalidCommand {
        addAll();
        
        BinaryString[] args = h(new String[]{"Nums", "1", "-5"});
        ZrangeCommand command = new ZrangeCommand(args);
        Iterator<Element> results = command.execute().value().getElements().iterator();
        
        Element element = results.next();
        assertEquals("Two", element.getMember());
        element = results.next();
        assertEquals("Three", element.getMember());
        assertFalse(results.hasNext());

    }

    @Test
    public void test_range_same_positive() throws InvalidCommand {
        addAll();
        
        BinaryString[] args = h(new String[]{"Nums", "3", "3"});
        ZrangeCommand command = new ZrangeCommand(args);
        Iterator<Element> results = command.execute().value().getElements().iterator();
        
        Element element = results.next();
        assertEquals("Four", element.getMember());
        assertFalse(results.hasNext());

    }

    @Test
    public void test_range_same_negative() throws InvalidCommand {
        addAll();
        
        BinaryString[] args = h(new String[]{"Nums", "-2", "-2"});
        ZrangeCommand command = new ZrangeCommand(args);
        Iterator<Element> results = command.execute().value().getElements().iterator();
        
        Element element = results.next();
        assertEquals("Seven", element.getMember());
        assertFalse(results.hasNext());

    }

    @Test
    public void test_range_with_scores() throws InvalidCommand {
        addAll();
        
        BinaryString[] args = h(new String[]{"Nums", "1", "4", "WITHSCORES"});
        ZrangeCommand command = new ZrangeCommand(args);
        assertTrue(command.execute().value().isScored());

    }

    @Test
    public void test_range_with__integer_scores() throws InvalidCommand {
        new ZaddCommand(ADD_INTEGER).execute();
        
        BinaryString[] args = h(new String[]{"Integer", "0", "1", "WITHSCORES"});
        ZrangeCommand command = new ZrangeCommand(args);
        Iterator<Element> results = command.execute().value().getElements().iterator();
        
        Element element = results.next();
        assertEquals("Tweleve", element.getMember());
        assertEquals(new Double(12.0), element.getScore());
        
        assertFalse(results.hasNext());
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
