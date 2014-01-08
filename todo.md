## Important ##
- Get length of reference sequence
  - Use this for the x-axis of the graphs

Do this by getting the reference index, then use SAMFileHeader.getSequence(), which returns a SAMSequenceRecord. Then use SAMSequenceRecord.getSequenceLength. BOOM! Then use this as the x-axis of the histogram.

- Color bars by quality score of the reference sequnce
  - PB read quality and assembly quality
- Include name and reference for ORF in stats file

## Less important ##

- replace .getReferenceName with .getReferenceIndex for speed
- replace SAMRecord with BAMRecord?
- Check for input files existing
- The references file is redundant if you include a regions file


## Error handling ##

- files not existing
- reference sequencing not being in the bam file
