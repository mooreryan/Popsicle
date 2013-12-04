# Popsicle #

Tasty viewing of bam files!

## Overview ##

Given a sorted bam file, an index file and a reference sequnce, Popsicle gives a histogram of read coverage for the given reference.

## Usage ##

java -jar popsicle-0.0.1-standalone.jar \ <br>
-b sorted-bam-file.bam -i bam-index.bam.bai -r reference\_sequence\_file.txt

### Reference sequence file ###

Enter the sequences you wish to view one per line in a text file like so: <br>
<br>
seq1 <br>
seq2 <br>
seq3 <br>
... <br>
seqN

## Notes ##

- The bam file must be sorted. Make your index from this sorted file.
- Opens charts in a new java window (make sure X11 forwarding is enabled if running through biohen)
- The coverage is displayed not across the entire length of the reference sequence, but rather only where there is actually coverage
- The coverage displayed is relative. It is actually the total coverage in each bin. The bin width is LengthRefSequence/100

##### Contact #####

Ryan Moore, Graduate Research Assistant <br>
University of Delaware <br>
Center for Bioinformatics and Computational Biology <br>
moorer@udel.edu <br>
