package jredis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import jredis.exception.InvalidCommand;
import jredis.exception.InvalidFileFormat;

import org.junit.Before;
import org.junit.Test;

public class SaveCommandTest {
    
    @Before
    public void setup() {
        DB.INSTANCE.clear();
    }
    
    @Test
    public void test_save() throws InvalidCommand, FileNotFoundException, InvalidFileFormat {
        String[] argsSet = {"Focus", "Ford"};
        String[] argsGet = {"Focus"};
        
        Command<?> command = new SetCommand(argsSet);
        command.execute();
        
        command = new SaveCommand(null);
        command.execute();
        
        File rdb = new File(Server.config(Server.DATA_DUMP));
        assertTrue(rdb.exists());
        
        DB.INSTANCE.clear();
        
        new Loader(new FileInputStream(rdb)).load();
        command = new GetCommand(argsGet);
        
        assertEquals("Ford", command.execute().value());
        
    }

}
