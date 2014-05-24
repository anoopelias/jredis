package jredis.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import jredis.DB;
import jredis.Protocol;
import jredis.Server;
import jredis.domain.BinaryString;
import jredis.exception.InvalidCommand;
import jredis.exception.InvalidFileFormat;
import jredis.rdf.Loader;

import org.junit.Before;
import org.junit.Test;

public class SaveCommandTest {
    
    @Before
    public void setup() {
        DB.INSTANCE.clear();
    }
    
    @Test
    public void test_save() throws InvalidCommand, FileNotFoundException, InvalidFileFormat {
        BinaryString[] argsSet = Protocol.toBinaryStrings(new String[]{"Focus", "Ford"});
        BinaryString[] argsGet = Protocol.toBinaryStrings(new String[]{"Focus"});
        
        Command<?> command = new SetCommand(argsSet);
        command.execute();
        
        command = new SaveCommand(new String[0]);
        command.execute();
        
        File rdb = new File(Server.config(Server.DATA_DUMP));
        assertTrue(rdb.exists());
        
        DB.INSTANCE.clear();
        
        new Loader(new FileInputStream(rdb)).load();
        command = new GetCommand(argsGet);
        
        assertEquals("Ford", command.execute().value());
        
    }

}
