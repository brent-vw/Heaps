library(readr)
library(ggplot2)

heaps <- read_delim("~/Projects/ugent/da2/verslag/resultaten_heaps.csv", 
                    ";", escape_double = FALSE, trim_ws = TRUE)
heaps_insert_10k <- heaps[heaps$algo=='insert_10k',]
heaps_insert_1mil <- heaps[heaps$algo=='insert_1mil',]

heaps_remove_min_10k <- heaps[heaps$algo=='remove_min_10k',]
heaps_remove_min_1mil <- heaps[heaps$algo=='remove_min_10mil',]

heaps_random_10k <- heaps[heaps$algo=='random_10k',]
heaps_random_1mil <- heaps[heaps$algo=='random_1mil',]

###Insert###
ggplot(heaps_insert_10k, aes(heap,time)) + 
  geom_bar(stat= "identity", aes(fill = data), width = 0.5 , position = position_dodge(width = 0.55)) +
  ylim(0, 100) +
  coord_flip() +
  labs(y="Tijd (ms)", x="",title="Insert", subtitle="10.000 operaties op een initiëel lege heap", fill="Dataset")

ggplot(heaps_insert_1mil, aes(heap,time)) + 
  geom_bar(stat= "identity", aes(fill = data), width = 0.5 , position = position_dodge(width = 0.55)) +
  coord_flip() +
  labs(y="Tijd (ms)", x="",title="Insert", subtitle="1.000.000 operaties op een initiëel lege heap", fill="Dataset")

###Remove###
ggplot(heaps_remove_min_10k, aes(heap,time)) + 
  geom_bar(stat= "identity", aes(fill = data), width = 0.5 , position = position_dodge(width = 0.55)) +
  ylim(0, 100) +
  coord_flip() +
  labs(y="Tijd (ms)", x="",title="Remove-Min", subtitle="10.000 operaties op een gevulde heap", fill="Dataset")

ggplot(heaps_remove_min_1mil, aes(heap,time)) + 
  geom_bar(stat= "identity", aes(fill = data), width = 0.5 , position = position_dodge(width = 0.55)) +
  coord_flip() +
  labs(y="Tijd (ms)", x="",title="Remove-Min", subtitle="1.000.000 operaties op een gevulde heap", fill="Dataset")

###Random###
ggplot(heaps_random_10k, aes(heap, time)) + 
  geom_bar(stat= "identity", aes(fill=data), width = 0.33333333333) +
  coord_flip() +
  guides(fill=FALSE) +
  labs(y="Tijd (ms)", x="",title="Random (remove-min, remove, update)", subtitle="10.000 operaties op een gevulde heap", fill="Dataset")

ggplot(heaps_random_1mil, aes(heap, time)) + 
  geom_bar(stat= "identity", aes(fill=data), width = 0.33333333333) +
  coord_flip() +
  guides(fill=FALSE) +
  labs(y="Tijd (ms)", x="",title="Random (remove-min, remove, update)", subtitle="1.000.000 operaties op een gevulde heap", fill="Dataset")




####################
##    Deel 2      ##
####################
heaps_operation <- read_delim("~/Projects/ugent/da2/verslag/heaps_operation.csv", 
                              ";", escape_double = FALSE, trim_ws = TRUE)

rem <- heaps_operation[heaps_operation$operation=='remove',]
inc <- heaps_operation[heaps_operation$operation=='increase',]
dcr <- heaps_operation[heaps_operation$operation=='decrease',]

ggplot(rem, aes(x=n,y=time,color=heap)) +
  geom_point() +
  geom_smooth(method = 'loess', span = 1.5, se=FALSE) +
  labs(y="Tijd (ms)", title="Remove", subtitle="Operaties op een reeds gevulde heap", x="n (x1000)", fill="Heap")

ggplot(inc, aes(x=n,y=time,color=heap)) +
  geom_point() +
  geom_smooth(method = 'loess', span = 1.5, se=FALSE) +
  labs(y="Tijd (ms)", title="Increase", subtitle="Operaties op een reeds gevulde heap", x="n (x1000)", fill="Heap")

ggplot(dcr, aes(x=n,y=time,color=heap)) +
  geom_point() +
  geom_smooth(method = 'loess', span = 1.5, se=FALSE) +
  labs(y="Tijd (ms)", title="Decrease", subtitle="Operaties op een reeds gevulde heap", x="n (x1000)", fill="Heap")

