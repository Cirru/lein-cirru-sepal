
lein-cirru-sepal
----

> Leiningen plugin for compiling Cirru code into Clojure.

Internally it uses [Sepal in Clojure](https://github.com/Cirru/sepal.clj) to transform code.

[Quick Start](https://github.com/Cirru/sepal.clj/wiki/Quick-Start).

### Usage

[![Clojars Project](http://clojars.org/cirru/lein-sepal/latest-version.svg)](http://clojars.org/cirru/lein-sepal)

Add `cirru/lein-sepal` in `project.clj`(choose higher versions):

```clj
:plugins [[cirru/lein-sepal "0.0.11"]]
```

Add configurations in your `project.clj` to set paths, for example:

```clj
:cirru-sepal {:paths ["cirru-src" "cirru-test"]}
```

It will compile `*.clj.cirru` to `*.clj`(or `*.cljs.cirru` to `.cljs`):

* from `cirru-src/` to `src/`
* from `cirru-test/` to `test/`

```bash
lein cirru-sepal # compile once
lein cirru-sepal watch # watch compiling
```

You may set extension to `.cljs` if needed.

### License

Copyright © 2015 jiyinyiyong

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
