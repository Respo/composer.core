
Composer Renderer
----

> Renderer library for Respo Composer

### Usage

[![Clojars Project](https://img.shields.io/clojars/v/respo/composer.svg)](https://clojars.org/respo/composer)

```edn
[respo/composer "0.1.1"]
```

```clojure
(respo-composer.core/render-markup markup
  {:data {}
   :level 1
   :templates {"container" nil}
   :cursor %cursor}
  (fn [dispatch! op param options]))
```

`options` contains `:event :props :data`.

```clojure
(respo-composer.core/extract-templates
  (read-string (shadow.resource/inline "composer.edn")))
```

### Specs

```
Type       props              Event
----

box                           param
space      width, height
divider    kind, color
text       value
some       value, kind
button     text               param
link       text, href         param
icon       name               param
template   name, data
list       value              param
input      value, textarea    param
slot       dom
inspect    title
popup      visible
case       value(?)
element    name
```

Props supports values in simple syntaxes:

* `:x` would be read as a keyword `:x`
* `"x` and `|x` would be read as a string `"x"`
* `8` is just a number
* `a`, means "a"
* `~:a` will be parsed into `:a`

and `@` is the instruction to read from data:

* `@:a`, means `(get-in data [:a])`
* `@:a :b` is like `(get-in scope [:a :b])`
* `@|a`, means `(get-in data ["a"])`
* `@:a :b |c`, means `(get-in data [:a :b "c"])`

data inside list children

* `index`
* `item`, list item
* `outer`, data of outer context

`some` instruction kinds:

* `nil` or `:value`, detect with `nil?`
* `:list`, detect with `empty?`
* `:boolean`, detect with `#(= "false" %)`

`space` props:

* `width`, number
* `height`, number

`divider` props:

* `kind`, defaults to horizontal, could be `:vertical`
* `color`, defaults to `#eee`

### Workflow

Workflow https://github.com/mvc-works/calcit-workflow

### License

MIT
