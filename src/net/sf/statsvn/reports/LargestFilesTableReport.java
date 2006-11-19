/*
	StatCvs - CVS statistics generation 
	Copyright (C) 2002  Lukasz Pekacki <lukasz@pekacki.de>
	http://statcvs.sf.net/
    
	This library is free software; you can redistribute it and/or
	modify it under the terms of the GNU Lesser General Public
	License as published by the Free Software Foundation; either
	version 2.1 of the License, or (at your option) any later version.

	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
	Lesser General Public License for more details.

	You should have received a copy of the GNU Lesser General Public
	License along with this library; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
    
	$RCSfile: LargestFilesTableReport.java,v $
	$Date: 2004/02/20 01:33:29 $
*/
package net.sf.statsvn.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import net.sf.statsvn.Messages;
import net.sf.statsvn.model.VersionedFile;
import net.sf.statsvn.reportmodel.FileColumn;
import net.sf.statsvn.reportmodel.IntegerColumn;
import net.sf.statsvn.reportmodel.Table;

/**
 * Table report for a table containing the files with most lines of code
 * 
 * @author Richard Cyganiak <rcyg@gmx.de>
 * @version $Id: LargestFilesTableReport.java,v 1.4 2004/02/20 01:33:29 cyganiak Exp $
 */
public class LargestFilesTableReport implements TableReport {
	private List files;
	private Table table;
	private int maxRows;

	/**
	 * Creates a table containing the largest files from a file list
	 * @param files a <tt>SortedSet</tt> of
	 *              {@link net.sf.statsvn.model.VersionedFile}s
	 * @param maxRows the maximum number of files displayed in the table 
	 */
	public LargestFilesTableReport(final SortedSet files, final int maxRows) {
		this.files = new ArrayList(files);
		this.maxRows = maxRows;
	}

	/**
	 * @see net.sf.statsvn.reports.TableReport#calculate()
	 */
	public void calculate() {
		Collections.sort(files, new FilesLocComparator());
		table = new Table(Messages.getString("LARGEST_FILES_TABLE_SUMMARY"));
		table.setKeysInFirstColumn(true);
		final FileColumn filesCol = new FileColumn();
		final IntegerColumn locCol = new IntegerColumn(Messages.getString("COLUMN_LOC"));
		locCol.setShowPercentages(false);
		table.addColumn(filesCol);
		table.addColumn(locCol);
		int lines = 0;
		final Iterator it = files.iterator();
		while (it.hasNext() && lines < maxRows) {
			final VersionedFile file = (VersionedFile) it.next();
			if (file.isDead()) {
				continue;
			}
			filesCol.addValue(file);
			locCol.addValue(file.getCurrentLinesOfCode());
			lines++;
		} 
	}

	/**
	 * @see net.sf.statsvn.reports.TableReport#getTable()
	 */
	public Table getTable() {
		return table;
	}
}