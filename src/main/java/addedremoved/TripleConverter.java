package addedremoved;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;

import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.sparql.Bindings;
import it.unibo.arces.wot.sepa.commons.sparql.RDFTermBNode;
import it.unibo.arces.wot.sepa.commons.sparql.RDFTermLiteral;
import it.unibo.arces.wot.sepa.commons.sparql.RDFTermURI;

public class TripleConverter {

	public static String nuplaToString(Bindings triple) throws SEPABindingsException {
		if(triple.getVariables().size()<1){
			return null;
		}
		String tripl = "";
		for (String var : triple.getVariables()) {
			tripl+=triple.isURI(var)? "<"+triple.getValue(var)+">": "\""+triple.getValue(var)+"\"" ;
			tripl+= " ";
		}				
		tripl+=" .";
		return tripl;
		
	} 
	
	public static String tripleToString(Bindings triple) throws SEPABindingsException {
		if(triple.getVariables().contains("s") && triple.getVariables().contains("p")  && triple.getVariables().contains("o") ) {
			String s =triple.isURI("s")? "<"+triple.getValue("s")+">": "\""+triple.getValue("s")+"\"";
			String p =triple.isURI("p")? "<"+triple.getValue("p")+">": "\""+triple.getValue("p")+"\"";
			String o =triple.isURI("o")? "<"+triple.getValue("o")+">": "\""+triple.getValue("o")+"\"";
			return s+ " "+ p + " " + o+ " .";
		}else {
			return null;
		}
		
	} 
	
	public static Triple bindingToTriple(Bindings bindings) throws SEPABindingsException{
		String subject = bindings.getValue(BindingTag.SUBJECT.toString());
		String predicate = bindings.getValue(BindingTag.PREDICATE.toString());
		String object = bindings.getValue(BindingTag.OBJECT.toString());			
		
		Node s = bindings.isBNode(BindingTag.SUBJECT.toString()) ? NodeFactory.createBlankNode(subject) : NodeFactory.createURI(subject);
		Node p = bindings.isBNode(BindingTag.PREDICATE.toString()) ? NodeFactory.createBlankNode(predicate) : NodeFactory.createURI(predicate);

		Node o = null;
		if(!bindings.isBNode(BindingTag.OBJECT.toString())){
			o = bindings.isURI(BindingTag.OBJECT.toString()) ? NodeFactory.createURI(object) : NodeFactory.createLiteral(object);
		}else{
			o = NodeFactory.createBlankNode(object);
		}

		return new Triple(s,p,o);
	}

	public static Bindings convertTripleToBindings(Triple t) {
		Bindings temp = new Bindings();
		if(t.getSubject().isLiteral()){
			temp.addBinding(BindingTag.SUBJECT.toString(), new RDFTermLiteral(t.getSubject().toString()));
		}else if(t.getSubject().isURI()) {
			temp.addBinding(BindingTag.SUBJECT.toString(), new RDFTermURI(t.getSubject().getURI()));			
		}else if(t.getSubject().isBlank()) {
			temp.addBinding(BindingTag.SUBJECT.toString(), new RDFTermBNode(t.getSubject().toString()));		
		}else {
			System.out.println("Warning, cannot convert Subject of Triple to Bindings, for triple: "+t.toString());
		}
		if(t.getPredicate().isLiteral()){
			temp.addBinding(BindingTag.PREDICATE.toString(), new RDFTermLiteral(t.getPredicate().toString()));
		}else if(t.getPredicate().isURI()) {
			temp.addBinding(BindingTag.PREDICATE.toString(), new RDFTermURI(t.getPredicate().getURI()));			
		}else if(t.getPredicate().isBlank()) {
			temp.addBinding(BindingTag.PREDICATE.toString(), new RDFTermBNode(t.getPredicate().toString()));		
		}else {
			System.out.println("Warning, cannot convert Predicate of Triple to Bindings, for triple: "+t.toString());
		}
		if(t.getObject().isLiteral()){
			temp.addBinding(BindingTag.OBJECT.toString(), new RDFTermLiteral(t.getObject().toString()));
		}else if(t.getObject().isURI()) {
			temp.addBinding(BindingTag.OBJECT.toString(), new RDFTermURI(t.getObject().getURI()));			
		}else if(t.getObject().isBlank()) {
			temp.addBinding(BindingTag.OBJECT.toString(), new RDFTermBNode(t.getObject().toString()));		
		}else {
			System.out.println("Warning, cannot convert Object of Triple to Bindings, for triple: "+t.toString());
		}
		return temp;
	}
	
	
	
}
