package factories;

import java.util.Set;

import core.test.MetaTest;


public interface IMetaTestFactory {
	Set<String> getTestNames();
	
	MetaTest getTestByName(String name);
}
