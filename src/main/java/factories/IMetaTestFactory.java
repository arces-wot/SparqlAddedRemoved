package factories;

import java.util.Set;

import core.MetaTest;


public interface IMetaTestFactory {
	Set<String> getTestNames();
	
	MetaTest getTestByName(String name);
}
