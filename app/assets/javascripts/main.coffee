# -----------------------------------------------
# MAIN

# Just a log helper
log = (args...) ->
  console.log.apply console, args if console.log?

# --------------------------------- EDIT IN PLACE
$.fn.editInPlace = (method, options...) ->
  this.each ->
    methods =
    # public methods
      init: (options) ->
        valid = (e) =>
          newValue = @input.val()
          options.onChange.call(options.context, newValue)
        cancel = (e) =>
          @$el.show()
          @input.hide()
        @$el = $(this).dblclick(methods.edit)
        @input = $("<input type='text' />")
          .insertBefore(@$el)
          .keyup (e) ->
            switch(e.keyCode)
            # Enter key
              when 13 then $(this).blur()
            # Escape key
              when 27 then cancel(e)
          .blur(valid)
          .hide()
      edit: ->
        @input
          .val(@$el.text())
          .show()
          .focus()
          .select()
        @$el.hide()
      close: (newName) ->
        @$el.text(newName).show()
        @input.hide()
    # jQuery approach: http://docs.jquery.com/Plugins/Authoring
    if ( methods[method] )
      return methods[ method ].apply(this, options)
    else if (typeof method == 'object')
      return methods.init.call(this, method)
    else
      $.error("Method " + method + " does not exist.")

# ---------------------------------------- ROUTER
class AppRouter extends Backbone.Router
  initialize: ->
    @currentApp = new CustomData
      el: $("#main")
  routes:
    ""                          : "index"
  index: ->
    # show dashboard
    $("#main").load "/ #main"

# ----------------------------------------- TASKS
class CustomData extends Backbone.View
  events:
    "click .deleteItem"             : "deleteItem"
    "submit .addItem"               : "newItem"
    "dblclick h4"                   : "editItem"
  render: (project) ->
    @project = project
    # HTML is our model
    @folders = $.map $(".folder", @$el), (folder) =>
      new TaskFolder
        el: $(folder)
        project: @project
  newItem: (e) =>
    e.preventDefault()
    $(document).focus() # temporary disable form
    form = $(e.target)
    @cdkey = $("input[name=key]", form).val()
    if @cdkey == "" || @cdkey.indexOf(" ") != -1
      alert "Custom data keys cannot be empty nor contain spaces"
      return
    @cdvalue = $("input[name=value]", form).val()
    if @cdvalue == "" || @cdvalue.indexOf(" ") != -1
      alert "Custom data values cannot be empty nor contain spaces"
      return
    if document.getElementById(@cdkey) != null
      alert('Key already exists')
      return
    r = jsRoutes.controllers.CustomDataController.addCustomDataItem()
    $.ajax
      url: r.url
      type: r.method
      context: this
      data:
        key: $("input[name=key]", form).val()
        value: $("input[name=value]", form).val()
      success: (tpl) ->
        customDataItem = new CustomDataItem(el: $(tpl))
        @$el.find("ul").append(customDataItem.el)
        form.find("input[type=text]").val("").first().focus()
      error: (err) ->
        alert "Something went wrong:" + err
    false
  deleteItem: (e) =>
    @id = e.currentTarget.attributes.getNamedItem("id").value
    e.preventDefault() if e?
    @loading(false)
    r = jsRoutes.controllers.CustomDataController.deleteCustomDataItem(@id)
    $.ajax
      url: r.url
      type: r.method
      context: this
      data:
        name: name
      success: (data) ->
        @loading(false)
        $("[customdata-item-id=" + this.id + "]").remove()
        @trigger("delete", @)
      error: (err) ->
        @loading(false)
        $.error("Error: " + err)
    false
  editItem: (e) =>
    e.preventDefault()
    # TODO
    alert "not implemented yet."
    false
  loading: (display) ->
    if (display)
      @$el.children(".options").hide()
      @$el.children(".loader").show()
    else
      @$el.children(".options").show()
      @$el.children(".loader").hide()

# ------------------------------------- TASK ITEM
class CustomDataItem extends Backbone.View
  initialize: (options) ->
    @id = @$el.attr("customdata-item-id")

# ------------------------------------- INIT APP
$ -> # document is ready!

  app = new AppRouter()

  Backbone.history.start
    pushHistory: true

