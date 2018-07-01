create keyspace if not exists retail_product with replication = {'class':'SimpleStrategy','replication_factor':1};


create table if not exists retail_product.product_by_id_name(
 product_id text,
 product_name text,
 data text,
 date timestamp,
 PRIMARY KEY(product_id,product_name)
) WITH CLUSTERING ORDER BY (product_name DESC)
AND gc_grace_seconds = 0
AND compression = {'sstable_compression':'SnappyCompressor'}
AND compaction = {
'class' : 'DateTiredCompactionStrategy',
'timestamp_resolution':'MILLISECONDS'
}