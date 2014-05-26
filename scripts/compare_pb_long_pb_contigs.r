## 20 february 2014
## comparing the different alignments from popsicle


############# now to compare pb_contigs and pb_long_reads

library(vioplot)

both <- 'T = yes\nWilcox = yes'
t.only <- 'T = yes\nWilcox = no'
wilcox.only <- 'T = no\nWilcox = yes'
neither <- 'T = no\nWilcox = no'

par(mfrow=c(2,3))

cont <- read.table('/Users/ryanmoore/Desktop/serc/popsicle_out/pb_contigs/bowtie2/serc.contigs.popsicle8_stats', header=T)
long <- read.table('/Users/ryanmoore/Desktop/serc/popsicle_out/pb_long_reads/bowtie2/all_serc_ill_reads_to_serc_pb_reads.popsicle10_stats', header=T)

t.test(cont$mean, long$mean)



wilcox.test(cont$mean, long$mean)



vioplot(cont$mean, long$mean, names=c('Contigs', 'Reads'))
text(x=c(0.8, 1.5, 2.2), y=rep(10000, 3),
     labels=c(round(mean(cont$mean)),
         both, round(mean(long$mean))))
title('Mean coverage for all contigs', ylab='Coverage')

# throws out 2167 of reads without coverage at all
no.nocov <- read.table('/Users/ryanmoore/Desktop/serc/popsicle_out/pb_long_reads/bowtie2/no_nocov', header=T)

t.test(cont$sd, no.nocov$sd)




wilcox.test(cont$sd, no.nocov$sd)



vioplot(cont$sd, no.nocov$sd, names=c('Contigs', 'Reads'))
text(x=c(0.8, 1.5, 2.2), y=rep(5000, 3),
     labels=c(round(mean(cont$sd)),
         both, round(mean(no.nocov$sd))))
title('SD of coverage for all contigs', ylab='Coverage')




t.test(cont$sd.mean, no.nocov$sd.mean)



wilcox.test(cont$sd.mean, no.nocov$sd.mean)




vioplot(cont$sd.mean, no.nocov$sd.mean, names=c('Contigs', 'Reads'))
text(x=c(0.8, 1.5, 2.2), y=rep(4, 3),
     labels=c(round(mean(cont$sd.mean), digits=4),
         both, round(mean(no.nocov$sd.mean), digits=4)))
title('SD / mean of coverage for all contigs', ylab='SD / mean')


t.test(cont$peak, no.nocov$peak)



wilcox.test(cont$peak, no.nocov$peak)



vioplot(cont$peak, no.nocov$peak, names=c('Contigs', 'Reads'))
text(x=c(0.8, 1.5, 2.2), y=rep(0.8, 3),
     labels=c(round(mean(cont$peak), digits=4),
         both, round(mean(no.nocov$peak), digits=4)))
title('Peak ratio for all contigs', ylab='num posns < mean / total posns')


t.test(cont$sign.change, no.nocov$sign.change)



wilcox.test(cont$sign.change, no.nocov$sign.change)




vioplot(cont$sign.change, no.nocov$sign.change, names=c('Contigs', 'Reads'))
text(x=c(0.8, 1.5, 2.2), y=rep(0.04, 3),
     labels=c(round(mean(cont$sign.change), digits=4),
         both, round(mean(no.nocov$sign.change), digits=4)))
title('Sign change ratio for all contigs', ylab='sign changes / total possible about mean')





