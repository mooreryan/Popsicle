## Important ##
- Get length of reference sequence
  - Use this for the x-axis of the graphs

Do this by getting the reference index, then use SAMFileHeader.getSequence(), which returns a SAMSequenceRecord. Then use SAMSequenceRecord.getSequenceLength. BOOM! Then use this as the x-axis of the histogram.

- Accept regions to overlay onto graph
- Color bars by quality score of the reference sequnce
  - PB read quality and assembly quality

## Less important ##

- replace .getReferenceName with .getReferenceIndex for speed
- replace SAMRecord with BAMRecord?
