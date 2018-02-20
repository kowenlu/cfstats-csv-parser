package com.jl.CfStatsParser.utils;

public class Constants {

	public static final String KEYSPACE = "Keyspace";
	public static final String COLUMNFAMILY = "Table";
	public static final String READCOUNT = "Local read count";
	public static final String READLATENCY = "Local read latency";
	public static final String WRITECOUNT = "Local write count";
	public static final String WRITELATENCY = "Local write latency";
	public static final String PENDINGTASKS = "Pending Tasks";
	public static final String SSTABLECOUNT = "SSTable count";
	public static final String SSTABLEIEL = "SSTables in each level";
	public static final String SPACEUSEDL = "Space used (live)";
	public static final String SPACEUSEDT = "Space used (total)";
	public static final String SSTABLECR = "SSTable Compression Ratio";
	public static final String NUMKEYS = "Number of Keys (estimate)";
	public static final String MEMTABLECC = "Memtable cell count";
	public static final String MEMTABLEDS = "Memtable data size, bytes";
	public static final String MEMTABLESC = "Memtable Switch Count";
	public static final String BLOOMFFP = "Bloom Filter False Positives";
	public static final String BLOOMFFR = "Bloom Filter False Ratio";
	public static final String BLOOMFSU = "Bloom Filter Space Used";
	public static final String COMPACTEDRMINS = "Compacted partition minimum bytes";
	public static final String COMPACTEDRMAXS = "Compacted partition maximum bytes";
	public static final String COMPACTEDRMEANS = "Compacted partition mean bytes";
	public static final String AVGLCPERSLICE = "Average live cells per slice (last five minutes)";
	public static final String AVGTPERSLICE = "Average tombstones per slice (last five minutes)";
	
}
