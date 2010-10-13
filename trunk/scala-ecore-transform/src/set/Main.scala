package set
import java.io._

import org.eclipse.emf.common.util._
import org.eclipse.emf.ecore._
import org.eclipse.emf.ecore.resource._
import org.eclipse.emf.ecore.resource.impl._
import org.eclipse.emf.ecore.xmi.impl._


object Main extends AnyRef with EmfModelSupport {
    def main (args: Array[String]) {
        val start = System.currentTimeMillis
        val model = readModel ("model/ExprDemo.ecore") 
        val afterRead = System.currentTimeMillis
        
        val out = new FileWriter ("src-gen/output.scala")
        model.eAllContents.typeSelect[EClass].foreach (cls => out.write (ClassGenerator (cls).classCode))
        out.close
        
        val afterGen = System.currentTimeMillis
        
        println ("finished generating.")
        println ("  " + (afterRead - start) + "ms for reading model")
        println ("  " + (afterGen - afterRead) + "ms for generating")
    }

    private def readModel(uri: String): EObject = {
        val reg = Resource.Factory.Registry.INSTANCE;
        val map = reg.getExtensionToFactoryMap();
        map.put("ecore", new XMIResourceFactoryImpl());

        val resourceSet = new ResourceSetImpl();
        val fileURI = URI.createFileURI(uri);
        val resource = resourceSet.getResource(fileURI, true);
        resource.getContents().get(0).asInstanceOf[EObject];
    }
}

