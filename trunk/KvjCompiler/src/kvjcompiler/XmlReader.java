/*
 *  KvJ Compiler for XML WZ data files
 *  Copyright (C) 2010  GoldenKevin
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package kvjcompiler;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XmlReader {
	private Converter c;
	private XMLStreamReader r;
	private FileOutputStream fos;
	
	public XmlReader(Converter c, XMLStreamReader r, FileOutputStream fos) {
		this.c = c;
		this.r = r;
		this.fos = fos;
	}
	
	public void traverseBlock() throws XMLStreamException, IOException {
		String tag = r.getLocalName();
		String name = r.getAttributeValue(0);
		int event;
		
		int open = 1;
		while (open > 0) {
			event = r.next();
			if (event == XMLStreamReader.START_ELEMENT) {
				open++;
				
				if (r.getLocalName().equals("imgdir")) {
					byte[] bytes = c.handleSpecial(name, r);
					if (bytes == null) {
						traverseInner(name);
					} else {
						fos.write(bytes);
					}
					event = r.getEventType();
				} else {
					String type = r.getLocalName();
					String key = name + "/" + r.getAttributeValue(0);
					String value = r.getAttributeValue(1);
					
					byte[] bytes = c.getEncodedBytes(key, value);
					if (bytes != null)
						fos.write(bytes);
					
					event = r.next();
					while (event == XMLStreamConstants.CHARACTERS)
						event = r.next();
					if (r.getLocalName() != type) {
						throw new IllegalStateException("ERROR: Expected '" + tag + "' to be closed. Received " + r.getLocalName() + " instead.");
					}
				}
			}
			if (event == XMLStreamReader.END_ELEMENT) {
				open--;
			}
		}
		
		if (r.getLocalName() != tag) //end block
			throw new IllegalStateException("ERROR: Expected '" + tag + "' to be closed. Received " + r.getLocalName() + " instead.");
	}
	
	private void traverseInner(String prev) throws XMLStreamException, IOException {
		String tag = r.getLocalName();
		String name = r.getAttributeValue(0);
		int event;
		
		int open = 1;
		while (open > 0) {
			event = r.next();
			if (event == XMLStreamReader.START_ELEMENT) {
				open++;
				
				if (r.getLocalName().equals("imgdir")) {
					traverseInner(prev + '/' + name);
					event = r.getEventType();
				} else {
					String type = r.getLocalName();
					String key = prev + '/' + name + '/' + r.getAttributeValue(0);
					String value = r.getAttributeValue(1);
		    		
					byte[] bytes = c.getEncodedBytes(key, value);
					if (bytes != null)
						fos.write(bytes);
		    		
					event = r.next();
					while (event == XMLStreamConstants.CHARACTERS)
						event = r.next();
					if (r.getLocalName() != type) {
						throw new IllegalStateException("ERROR: Expected '" + tag + "' to be closed. Received " + r.getLocalName() + " instead.");
					}
				}
			}
			if (event == XMLStreamReader.END_ELEMENT) {
				open--;
		}
	}
    
	if (r.getLocalName() != tag) //end block
		throw new IllegalStateException("ERROR: Expected '" + tag + "' to be closed. Received " + r.getLocalName() + " instead.");
	}
}
