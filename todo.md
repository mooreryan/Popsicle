## Important ##

- signal processing to find peaks
- overlay ORF boundries without calculating stats
- measure width of peaks in relation to ORF lengths
- smoothing algorithms

## Less important ##


- replace .getReferenceName with .getReferenceIndex for speed
- replace SAMRecord with BAMRecord?


## Error handling ##

- files not existing
- reference sequencing not being in the bam file

## Stuff ##


## Algorithms ##

(from PMC2628906)

### Window scoring ###

- Sort reads
- Note centered positions
- Deal with paired ends?

- Get first and last mapped read within a defined max window size -> this is the window size
- Move to next mapped read and last read falling within max window size
- If position is same as prior window termini, skip cos this collection is a subset of the prior
- Get all unique windows in this way and calculate the window level stats

### Window level stats ###

#### Simple sum ####

- Set each read score to 1
- The score is thus the number of reads within the window

### List of enriched regions/candidate binding peaks ###

- Pick an enrichment threshold and maximum gap
- Merge windows passing the threshold and with ends within the maximum gap
- Keep scores from best window as the score for the region and make a ranked list

### Global Poisson p-values ###

pi = 1 - sum(from= j=0, to= Yi-1, of= (labmda * e^-lambda) / j! )
where Yi is equal to the number of reads falling within the window i
and lambda equal to the expected number of reads to map to the window
by random chance. i.e.
(size of the window * total number reads/effective genome size (0.9 * genome size))
(see PMID: 17558387)

A conservative multiple testing correction is made following
Bonferroni [14] by multiplying each p-value by the number of window
tests.
