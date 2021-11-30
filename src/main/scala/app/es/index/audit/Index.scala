package app.es.index.audit

import com.fasterxml.jackson.annotation.JsonProperty

case class Index(status: String,
                 @JsonProperty(value = "store.size")
                 storeSize: String, @JsonProperty(value = "pri.store.size")
                 priStoreSize: Int,
                 @JsonProperty(value = "docs.count")
                 docsCount: Int,
                 @JsonProperty(value = "docs.deleted")
                 docsDeleted: Int,
                 health: String,
                 index: String,
                 uuid: String,
                 pri: String,
                 rep: String
                )