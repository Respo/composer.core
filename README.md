
> Check [Composer](https://github.com/Respo/composer) if you want to read source code. This is a repo only for documentations. However this repo contains an older version of respo-composer.core.

## Composer Renderer, Renderer library for Respo Composer

### Usage

[![Clojars Project](https://img.shields.io/clojars/v/respo/composer.svg)](https://clojars.org/respo/composer)

```edn
[respo/composer "0.1.10"]
```

```clojure
(respo-composer.core/render-markup markup
  {:data {}
   :level 1
   :templates {"container" nil}

   ; states options
   :state-fns {}
   :state-path []
   :states []}

  (fn [dispatch! op context options]))
```

`options` contains `:event :props :data :param`.

```clojure
(respo-composer.core/extract-templates
  (read-string (shadow.resource/inline "composer.edn")))
```

States mechanism is currently being explored, check [example in composer-todolist](https://github.com/Erigeron/composer-todolist/blob/master/src/app/vm.cljs).

### Specs

Type | props | Event
--- | --- | ---
box | | param
space | width, height
divider | kind, color
text | value, data
some | value, kind
button | text | param
link | text, href | param
icon | name, size, color | param
template | name, data |
list | value | param
input | value, textarea | param
function | name | param
inspect | value
popup | visible
case | value, options
element | name | param
markdown | text, class
image | src, mode, width, height
case | value, options

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

`#` is the instruction for reading states:

* `#:a`, means `(get-in (:data states) [:a])`

data inside list children

* `index`
* `item`, list item
* `outer`, data of outer context

`some` instruction kinds:

* `nil` or `:value`, detect with `nil?`
* `:list`, `:map`, detect with `empty?`
* `:string`, detect with `not . string/blank?`
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
