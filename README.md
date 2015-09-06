
lein-cirru-sepal
----

Leiningen plugin for compiling Cirru Sepal code into Clojure.

### Usage

[![Clojars Project](http://clojars.org/cirru/lein-sepal/latest-version.svg)](http://clojars.org/cirru/lein-sepal)

Add `cirru/lein-sepal` in `project.clj`(choose higher versions):

```clj
:plugins [[cirru/lein-sepal "0.0.1"]]
```

It compiles `*.cirru` in `source/` to `*.clj` into `src/`.

```bash
lein cirru-sepal # compile once
lein cirru-sepal watch # watch compiling
```

### License

Copyright © 2015 jiyinyiyong

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
