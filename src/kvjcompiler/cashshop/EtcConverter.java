/*
 *  KvJ Compiler for XML WZ data files
 *  Copyright (C) 2010-2013  GoldenKevin
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

package kvjcompiler.cashshop;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import kvjcompiler.Converter;

/**
 *
 * @author GoldenKevin
 */
public class EtcConverter extends Converter {
	@Override
	public String getWzName() {
		return "Etc.wz";
	}

	@Override
	public void compile(String outPath, String internalPath, String imgName, XMLStreamReader r) throws XMLStreamException, IOException {
		//don't worry about not using a singleton, we're only instantiating each once (only one copy of each file)
		if (imgName.equals("Commodity.img"))
			new CommodityConverter().compile(outPath, internalPath, imgName, r);
		else if (imgName.equals("CashPackage.img"))
			new CashPackageConverter().compile(outPath, internalPath, imgName, r);
	}

	@Override
	protected void handleDir(String nestedPath) throws XMLStreamException, IOException {

	}

	@Override
	protected void handleProperty(String nestedPath, String value) throws IOException {

	}
}
