## 7 february 2014
## comparing the different alignments from popsicle

both <- 'T = yes\nWilcox = yes'
t.only <- 'T = yes\nWilcox = no'
wilcox.only <- 'T = no\nWilcox = yes'
neither <- 'T = no\nWilcox = no'

par(mfrow=c(2,3))

############# comparing means of bowtie2 and bwa on pb contigs

bow <- read.table('/Users/ryanmoore/Desktop/serc/popsicle_out/pb_contigs/bowtie2/serc.contigs.popsicle8_stats', header=T)
bwa <- read.table('/Users/ryanmoore/Desktop/serc/popsicle_out/pb_contigs/bwa/serc_ill_reads_to_serc_pb_contigs.popsicle10_stats', header=T)

t.test(bow$mean, bwa$mean)

## 	Welch Two Sample t-test

## data:  bow$mean and bwa$mean
## t = 15.3668, df = 14192.12, p-value < 2.2e-16
## alternative hypothesis: true difference in means is not equal to 0
## 95 percent confidence interval:
##  105.3663 136.1765
## sample estimates:
## mean of x mean of y 
##  314.3971  193.6256 

wilcox.test(bow$mean, bwa$mean)

## 	Wilcoxon rank sum test with continuity correction

## data:  bow$mean and bwa$mean
## W = 45601270, p-value < 2.2e-16
## alternative hypothesis: true location shift is not equal to 0

vioplot(bow$mean, bwa$mean, names=c('bowtie2', 'bwa'))
text(x=c(0.8, 1.5, 2.2), y=rep(10000, 3),
     labels=c(round(mean(bow$mean)),
         both, round(mean(bwa$mean))))
title('Mean coverage for all PB contigs', ylab='Coverage')

## standard devs

t.test(bow$sd, bwa$sd)

## 	Welch Two Sample t-test

## data:  bow$sd and bwa$sd
## t = 11.8835, df = 14152.66, p-value < 2.2e-16
## alternative hypothesis: true difference in means is not equal to 0
## 95 percent confidence interval:
##  55.92660 78.02055
## sample estimates:
## mean of x mean of y 
##  183.4089  116.4353 

wilcox.test(bow$sd, bwa$sd)

## 	Wilcoxon rank sum test with continuity correction

## data:  bow$sd and bwa$sd
## W = 43492439, p-value < 2.2e-16
## alternative hypothesis: true location shift is not equal to 0

vioplot(bow$sd, bwa$sd, names=c('bowtie2', 'bwa'))
text(x=c(0.8, 1.5, 2.2), y=rep(5000, 3),
     labels=c(round(mean(bow$sd)),
         both, round(mean(bwa$sd))))
title('SD of coverage for all PB contigs', ylab='Coverage')

## SD / mean

t.test(bow$sd.mean, bwa$sd.mean)

## 	Welch Two Sample t-test

## data:  bow$sd.mean and bwa$sd.mean
## t = -5.0022, df = 15541.09, p-value = 5.73e-07
## alternative hypothesis: true difference in means is not equal to 0
## 95 percent confidence interval:
##  -0.03183902 -0.01391158
## sample estimates:
## mean of x mean of y 
## 0.4988536 0.5217289 

wilcox.test(bow$sd.mean, bwa$sd.mean)

## 	Wilcoxon rank sum test with continuity correction

## data:  bow$sd.mean and bwa$sd.mean
## W = 35172738, p-value = 0.2441
## alternative hypothesis: true location shift is not equal to 0


vioplot(bow$sd.mean, bwa$sd.mean, names=c('bowtie2', 'bwa'))
text(x=c(0.8, 1.5, 2.2), y=rep(4, 3),
     labels=c(round(mean(bow$sd.mean), digits=4),
         t.only, round(mean(bwa$sd.mean), digits=4)))
title('SD / mean of coverage for all PB contigs', ylab='SD / mean')

## peak

t.test(bow$peak, bwa$peak)

## 	Welch Two Sample t-test

## data:  bow$peak and bwa$peak
## t = -21.3205, df = 16844.85, p-value < 2.2e-16
## alternative hypothesis: true difference in means is not equal to 0
## 95 percent confidence interval:
##  -0.03870622 -0.03218849
## sample estimates:
## mean of x mean of y 
## 0.5111130 0.5465603 


wilcox.test(bow$peak, bwa$peak)

## 	Wilcoxon rank sum test with continuity correction

## data:  bow$peak and bwa$peak
## W = 28786626, p-value < 2.2e-16
## alternative hypothesis: true location shift is not equal to 0

vioplot(bow$peak, bwa$peak, names=c('bowtie2', 'bwa'))
text(x=c(0.8, 1.5, 2.2), y=rep(0.8, 3),
     labels=c(round(mean(bow$peak), digits=4),
         both, round(mean(bwa$peak), digits=4)))
title('Peak ratio for all PB contigs', ylab='num posns < mean / total posns')

## sign.change

t.test(bow$sign.change, bwa$sign.change)

## 	Welch Two Sample t-test

## data:  bow$sign.change and bwa$sign.change
## t = -21.3173, df = 16506.62, p-value < 2.2e-16
## alternative hypothesis: true difference in means is not equal to 0
## 95 percent confidence interval:
##  -0.002804483 -0.002332173
## sample estimates:
##   mean of x   mean of y 
## 0.008459243 0.011027571 

wilcox.test(bow$sign.change, bwa$sign.change)

## 	Wilcoxon rank sum test with continuity correction

## data:  bow$sign.change and bwa$sign.change
## W = 28258382, p-value < 2.2e-16
## alternative hypothesis: true location shift is not equal to 0

vioplot(bow$sign.change, bwa$sign.change, names=c('bowtie2', 'bwa'))
text(x=c(0.8, 1.5, 2.2), y=rep(0.05, 3),
     labels=c(round(mean(bow$sign.change), digits=4),
         both, round(mean(bwa$sign.change), digits=4)))
title('Sign change ratio for all PB contigs', ylab='sign changes / total possible about mean')

############# now to compare pb_contigs and illumina contigs

par(mfrow=c(2,3))

## pb <- read.table('/Users/ryanmoore/Desktop/serc/popsicle_out/serc.popsicle_stats8', header=T)
## ill <- read.table('/Users/ryanmoore/Desktop/serc/popsicle_out/illumina_contigs/bowtie2/serc_ill_reads_to_serc_ill_contigs.popsicle10_stats_all', header=T)

t.test(pb$mean, ill$mean)

## 	Welch Two Sample t-test

## data:  pb$mean and ill$mean
## t = 43.5872, df = 8447.564, p-value < 2.2e-16
## alternative hypothesis: true difference in means is not equal to 0
## 95 percent confidence interval:
##  277.1246 303.2246
## sample estimates:
## mean of x mean of y 
## 314.39706  24.22244 


wilcox.test(pb$mean, ill$mean)

## 	Wilcoxon rank sum test with continuity correction

## data:  pb$mean and ill$mean
## W = 1187448492, p-value < 2.2e-16
## alternative hypothesis: true location shift is not equal to 0


vioplot(pb$mean, ill$mean, names=c('PacBio', 'Illumina'))
text(x=c(0.8, 1.5, 2.2), y=rep(10000, 3),
     labels=c(round(mean(pb$mean)),
         both, round(mean(ill$mean))))
title('Mean coverage for all contigs', ylab='Coverage')


t.test(pb$sd, ill$sd)

## 	Welch Two Sample t-test

## data:  pb$sd and ill$sd
## t = 35.6581, df = 8455.174, p-value < 2.2e-16
## alternative hypothesis: true difference in means is not equal to 0
## 95 percent confidence interval:
##  161.1217 179.8670
## sample estimates:
## mean of x mean of y 
##  183.4089   12.9145 


wilcox.test(pb$sd, ill$sd)

## 	Wilcoxon rank sum test with continuity correction

## data:  pb$sd and ill$sd
## W = 1172355954, p-value < 2.2e-16
## alternative hypothesis: true location shift is not equal to 0


vioplot(pb$sd, ill$sd, names=c('PacBio', 'Illumina'))
text(x=c(0.8, 1.5, 2.2), y=rep(5000, 3),
     labels=c(round(mean(pb$sd)),
         both, round(mean(ill$sd))))
title('SD of coverage for all contigs', ylab='Coverage')


t.test(pb$sd.mean, ill$sd.mean)

## 	Welch Two Sample t-test

## data:  pb$sd.mean and ill$sd.mean
## t = 9.8454, df = 9054.342, p-value < 2.2e-16
## alternative hypothesis: true difference in means is not equal to 0
## 95 percent confidence interval:
##  0.02185213 0.03271686
## sample estimates:
## mean of x mean of y 
## 0.4988536 0.4715691 



wilcox.test(pb$sd.mean, ill$sd.mean)

## 	Wilcoxon rank sum test with continuity correction

## data:  pb$sd.mean and ill$sd.mean
## W = 625866348, p-value = 0.001408
## alternative hypothesis: true location shift is not equal to 0



vioplot(pb$sd.mean, ill$sd.mean, names=c('PacBio', 'Illumina'))
text(x=c(0.8, 1.5, 2.2), y=rep(4, 3),
     labels=c(round(mean(pb$sd.mean), digits=4),
         both, round(mean(ill$sd.mean), digits=4)))
title('SD / mean of coverage for all contigs', ylab='SD / mean')


t.test(pb$peak, ill$peak)

## 	Welch Two Sample t-test

## data:  pb$peak and ill$peak
## t = -2.6689, df = 9237.96, p-value = 0.007622
## alternative hypothesis: true difference in means is not equal to 0
## 95 percent confidence interval:
##  -0.0056512189 -0.0008651961
## sample estimates:
## mean of x mean of y 
## 0.5111130 0.5143712 



wilcox.test(pb$peak, ill$peak)

## 	Wilcoxon rank sum test with continuity correction

## data:  pb$peak and ill$peak
## W = 596846932, p-value = 3.718e-05
## alternative hypothesis: true location shift is not equal to 0



vioplot(pb$peak, ill$peak, names=c('PacBio', 'Illumina'))
text(x=c(0.8, 1.5, 2.2), y=rep(0.8, 3),
     labels=c(round(mean(pb$peak), digits=4),
         both, round(mean(ill$peak), digits=4)))
title('Peak ratio for all contigs', ylab='num posns < mean / total posns')


t.test(pb$sign.change, ill$sign.change)

## 	Welch Two Sample t-test

## data:  pb$sign.change and ill$sign.change
## t = -34.2276, df = 9129.618, p-value < 2.2e-16
## alternative hypothesis: true difference in means is not equal to 0
## 95 percent confidence interval:
##  -0.002905897 -0.002591084
## sample estimates:
##   mean of x   mean of y 
## 0.008459243 0.011207734 



wilcox.test(pb$sign.change, ill$sign.change)

## 	Wilcoxon rank sum test with continuity correction

## data:  pb$sign.change and ill$sign.change
## W = 418490506, p-value < 2.2e-16
## alternative hypothesis: true location shift is not equal to 0


vioplot(pb$sign.change, ill$sign.change, names=c('PacBio', 'Illumina'))
text(x=c(0.8, 1.5, 2.2), y=rep(0.05, 3),
     labels=c(round(mean(pb$sign.change), digits=4),
         both, round(mean(ill$sign.change), digits=4)))
title('Sign change ratio for all contigs', ylab='sign changes / total possible about mean')





